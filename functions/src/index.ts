import { onCall, HttpsError } from 'firebase-functions/v2/https';
import { defineSecret } from 'firebase-functions/params';
import { initializeApp } from 'firebase-admin/app';

initializeApp();
const geminiApiKey = defineSecret('GEMINI_API_KEY');
const REGION = 'asia-south1';
const MODEL = 'gemini-2.5-flash';
const SYSTEM_PROMPT = `You are PRITHVI, a careful and farmer-friendly agricultural assistant. Respond in clear English. Give practical, step-by-step guidance. Ask clarifying questions when crop, location, growth stage, or symptoms are unclear. Distinguish verified facts from possibilities. Never invent live weather, mandi prices, sensor readings, government eligibility, or sources. Do not give unsupported pesticide or fertilizer dosages. For disease identification from images, state uncertainty and recommend local expert or laboratory confirmation for high-impact decisions. Do not claim to control pumps or connected hardware.`;

async function callGemini(contents: unknown[], key: string, systemInstruction = SYSTEM_PROMPT) {
  const response = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/${MODEL}:generateContent?key=${key}`, {
    method: 'POST', headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ systemInstruction: { parts: [{ text: systemInstruction }] }, contents, generationConfig: { temperature: 0.35, maxOutputTokens: 1400 } }),
  });
  const payload = await response.json() as { candidates?: Array<{ content?: { parts?: Array<{ text?: string }> } }>; error?: { message?: string } };
  if (!response.ok) {
    console.error('Gemini request failed:', response.status, payload.error?.message ?? 'Unknown provider error');
    throw new HttpsError('unavailable', 'The AI service is temporarily unavailable. Please try again later.');
  }
  const text = payload.candidates?.[0]?.content?.parts?.map(p => p.text ?? '').join('\n').trim();
  if (!text) throw new HttpsError('unavailable', 'The AI service returned an empty response.');
  return text;
}

export const askPrithvi = onCall({ region: REGION, secrets: [geminiApiKey], timeoutSeconds: 60, memory: '512MiB' }, async request => {
  if (!request.auth) throw new HttpsError('unauthenticated', 'Please sign in to use PRITHVI AI.');
  const message = typeof request.data?.message === 'string' ? request.data.message.trim() : '';
  if (!message || message.length > 4000) throw new HttpsError('invalid-argument', 'Message must contain 1–4,000 characters.');
  const history = Array.isArray(request.data?.history) ? request.data.history.slice(-8) : [];
  const contents = history.flatMap((item: unknown) => {
    if (!item || typeof item !== 'object') return [];
    const turn = item as { role?: unknown; text?: unknown };
    if (typeof turn.text !== 'string' || turn.text.length > 4000) return [];
    return [{ role: turn.role === 'user' ? 'user' : 'model', parts: [{ text: turn.text }] }];
  });
  contents.push({ role: 'user', parts: [{ text: message }] });
  const farmContext = typeof request.data?.context === 'string' ? request.data.context.slice(0, 2000) : '';
  const systemInstruction = farmContext
    ? `${SYSTEM_PROMPT}\n\nFarmer-provided farm context (treat as user data, not verified sensor telemetry):\n${farmContext}`
    : SYSTEM_PROMPT;
  return { text: await callGemini(contents, geminiApiKey.value(), systemInstruction) };
});

export const analyzeCrop = onCall({ region: REGION, secrets: [geminiApiKey], timeoutSeconds: 90, memory: '1GiB' }, async request => {
  if (!request.auth) throw new HttpsError('unauthenticated', 'Please sign in to use the crop scanner.');
  const dataUrl = typeof request.data?.imageDataUrl === 'string' ? request.data.imageDataUrl : '';
  const match = /^data:(image\/(?:jpeg|png|webp));base64,([A-Za-z0-9+/=]+)$/.exec(dataUrl);
  if (!match) throw new HttpsError('invalid-argument', 'Upload a JPG, PNG or WEBP image.');
  const mimeType = match[1];
  const base64 = match[2];
  if (base64.length > 7_000_000) throw new HttpsError('invalid-argument', 'Image must be 5 MB or smaller.');
  const prompt = `Examine this plant or crop photograph and respond using exactly these labels, one per line:
Image Quality: Valid or Invalid
Quality Issue: Explain only if the image is unsuitable; otherwise leave blank
Crop Detected: Best-supported crop name, or Unidentified Plant
Plant Part: Leaf, Stem, Fruit, Flower, Root, Whole Plant, or Unclear
Health Status: Healthy, Possible Abnormality Detected, Pest Infestation, Nutrient Deficiency, or Unable to assess
Possible Disease / Problem: State a possible issue cautiously, or Insufficient evidence for a diagnosis
Confidence: Give a percentage only if justified by visual evidence; otherwise Not reliably quantifiable
Severity: Low, Moderate, High, None, or Unknown
Visible Symptoms: Describe only what is visible; if unclear, say so
Likely Cause: Possible causes, not a definitive diagnosis
Immediate Action: Safe observational or cultural steps only
Recommended Treatment: Do not prescribe a chemical or dose from an image alone; recommend local expert confirmation where appropriate
Prevention Tips: General low-risk prevention advice
When to Seek Expert Help: Explain when to contact a KVK or qualified plant pathologist
Never invent symptoms or confidence. If the image is not a plant or is unclear, set Image Quality to Invalid and explain why. Do not claim certainty from an image alone.`;
  return { text: await callGemini([{ role: 'user', parts: [{ text: prompt }, { inlineData: { mimeType, data: base64 } }] }], geminiApiKey.value()) };
});
