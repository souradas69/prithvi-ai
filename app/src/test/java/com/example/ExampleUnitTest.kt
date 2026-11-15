package com.example

import android.speech.RecognizerIntent
import com.example.data.IndianCropDiseaseRepository
import com.example.data.voice.IndianVoiceRecognitionManager
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {

  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun indianVoiceRecognitionManager_containsAllKeyIndianLanguages() {
    val languages = IndianVoiceRecognitionManager.supportedLanguages
    assertTrue("Should support at least 12 Indian languages", languages.size >= 12)

    val languageCodes = languages.map { it.code }
    assertTrue(languageCodes.contains("hi"))
    assertTrue(languageCodes.contains("bn"))
    assertTrue(languageCodes.contains("ta"))
    assertTrue(languageCodes.contains("te"))
    assertTrue(languageCodes.contains("mr"))
    assertTrue(languageCodes.contains("gu"))
    assertTrue(languageCodes.contains("pa"))
    assertTrue(languageCodes.contains("kn"))
    assertTrue(languageCodes.contains("ml"))
    assertTrue(languageCodes.contains("or"))
    assertTrue(languageCodes.contains("ur"))
    assertTrue(languageCodes.contains("en"))
  }

  @Test
  fun indianVoiceRecognitionManager_createsValidSpeechIntent() {
    val hindiLang = IndianVoiceRecognitionManager.getLanguageByDisplayName("Hindi")
    assertNotNull(hindiLang)
    assertEquals("Hindi", hindiLang.englishName)

    val intent = IndianVoiceRecognitionManager.createSpeechIntent(hindiLang, "Tomato")
    assertEquals(RecognizerIntent.ACTION_RECOGNIZE_SPEECH, intent.action)
    assertEquals("hi-IN", intent.getStringExtra(RecognizerIntent.EXTRA_LANGUAGE))
    val prompt = intent.getStringExtra(RecognizerIntent.EXTRA_PROMPT)
    assertNotNull(prompt)
    assertTrue("Prompt should contain language or query hint", prompt!!.isNotBlank())
  }

  @Test
  fun indianCropDiseaseRepository_providesComprehensiveData() {
    val diseases = IndianCropDiseaseRepository.allDiseases
    assertTrue("Should contain common Indian crop diseases", diseases.isNotEmpty())

    val riceDiseases = IndianCropDiseaseRepository.getDiseasesForCrop("Rice")
    assertTrue("Rice should have documented diseases", riceDiseases.isNotEmpty())
    val bacterialBlight = riceDiseases.firstOrNull { it.diseaseNameEn.contains("Bacterial Leaf Blight", ignoreCase = true) }
    assertNotNull("Should have Bacterial Leaf Blight for Rice", bacterialBlight)
    assertTrue("Should have non-empty Hindi name", bacterialBlight!!.diseaseNameHi.isNotBlank())
    assertTrue("Should have non-empty Bengali name", bacterialBlight.diseaseNameBn.isNotBlank())
  }
}


