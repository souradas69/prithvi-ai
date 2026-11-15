package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.IndianCropDiseaseRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("PRITHVI AI", appName)
  }

  @Test
  fun `verify indian crop disease dictionary lookup`() {
    val diseases = IndianCropDiseaseRepository.allDiseases
    assertTrue(diseases.isNotEmpty())

    // Test Rice BLB lookup
    val riceMatch = IndianCropDiseaseRepository.findMatchingDisease("Rice", "Bacterial Leaf Blight")
    assertNotNull(riceMatch)
    assertEquals("धान", riceMatch?.cropNameHi)
    assertTrue(riceMatch?.treatment?.immediateStepHi?.isNotEmpty() == true)

    // Test Tomato lookup
    val tomatoMatch = IndianCropDiseaseRepository.findMatchingDisease("Tomato", "Early Blight")
    assertNotNull(tomatoMatch)
    assertEquals("टमाटर", tomatoMatch?.cropNameHi)
  }
}
