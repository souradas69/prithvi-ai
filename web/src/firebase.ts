import { initializeApp, getApps, type FirebaseOptions } from 'firebase/app';
import { getAuth } from 'firebase/auth';
import { getFirestore } from 'firebase/firestore';
import { getFunctions } from 'firebase/functions';

const env = import.meta.env;
const apiKey = env.VITE_FIREBASE_API_KEY?.trim();
const authDomain = env.VITE_FIREBASE_AUTH_DOMAIN?.trim();
const projectId = env.VITE_FIREBASE_PROJECT_ID?.trim();
const appId = env.VITE_FIREBASE_APP_ID?.trim();
const storageBucket = env.VITE_FIREBASE_STORAGE_BUCKET?.trim();
const messagingSenderId = env.VITE_FIREBASE_MESSAGING_SENDER_ID?.trim();

export const missingFirebaseFields = Object.entries({
  VITE_FIREBASE_API_KEY: apiKey,
  VITE_FIREBASE_AUTH_DOMAIN: authDomain,
  VITE_FIREBASE_PROJECT_ID: projectId,
  VITE_FIREBASE_APP_ID: appId,
}).filter(([, value]) => !value).map(([key]) => key);

export const firebaseConfigured = missingFirebaseFields.length === 0;
const config: FirebaseOptions = {
  apiKey: apiKey ?? '',
  authDomain: authDomain ?? '',
  projectId: projectId ?? '',
  appId: appId ?? '',
  ...(storageBucket ? { storageBucket } : {}),
  ...(messagingSenderId ? { messagingSenderId } : {}),
};

const existingApp = firebaseConfigured
  ? getApps().find(candidate => candidate.options.projectId === projectId)
  : undefined;
const app = firebaseConfigured ? (existingApp ?? initializeApp(config)) : null;

export const auth = app ? getAuth(app) : null;
export const db = app ? getFirestore(app) : null;
export const functions = app ? getFunctions(app, 'asia-south1') : null;
