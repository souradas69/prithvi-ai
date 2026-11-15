package com.example.data.repository

data class GovtScheme(
    val id: String,
    val title: String,
    val shortDescription: String,
    val benefits: String,
    val eligibility: String,
    val requiredDocuments: List<String>,
    val applicationProcess: String,
    val isCentral: Boolean,
    val stateName: String? = null,
    val officialSource: String = "pmkisan.gov.in / Ministry of Agriculture",
    val lastUpdated: String = "August 2026",
    val officialUrl: String = "https://agricoop.gov.in"
)

object GovernmentSchemeRepository {

    fun getSchemesForFarmer(state: String): List<GovtScheme> {
        val schemes = mutableListOf<GovtScheme>()

        // Central schemes
        schemes.add(
            GovtScheme(
                id = "pm_kisan",
                title = "PM Kisan Samman Nidhi",
                shortDescription = "Direct income support of ₹6,000 per year in three equal installments of ₹2,000 for landholding farmer families.",
                benefits = "₹6,000 / year transferred directly into bank account (Aadhaar linked DBTs).",
                eligibility = "All landholding farmer families with cultivable land in their names.",
                requiredDocuments = listOf("Aadhaar Card", "Land Ownership Documents (Khatian/Patta)", "Bank Passbook with IFSC", "Mobile Number"),
                applicationProcess = "Apply online via PM-Kisan Portal or visit nearest Common Service Centre (CSC) / Krishi Bhavan.",
                isCentral = true,
                officialSource = "pmkisan.gov.in",
                lastUpdated = "August 2026"
            )
        )

        schemes.add(
            GovtScheme(
                id = "pmfby",
                title = "PM Fasal Bima Yojana (Crop Insurance)",
                shortDescription = "Comprehensive crop risk insurance coverage against yield loss due to non-preventable natural risks.",
                benefits = "Low premium rates (1.5% for Rabi, 2% for Kharif, 5% for commercial crops). Full financial coverage for crop loss.",
                eligibility = "Farmers growing notified crops in notified areas (both sharecroppers and tenant farmers eligible).",
                requiredDocuments = listOf("Land Sowing Certificate", "Aadhaar Card", "Bank Account Details", "Land Possession Proof"),
                applicationProcess = "Enroll via Banks, CSCs, or directly through PMFBY portal / Farmer App before sowing cut-off date.",
                isCentral = true,
                officialSource = "pmfby.gov.in",
                lastUpdated = "August 2026"
            )
        )

        schemes.add(
            GovtScheme(
                id = "kcc",
                title = "Kisan Credit Card (KCC)",
                shortDescription = "Concessional institutional credit for agricultural inputs, crop maintenance, and post-harvest expenses.",
                benefits = "Collateral-free loan up to ₹1.6 Lakh (up to ₹3 Lakh with 3% prompt repayment incentive, net interest 4%).",
                eligibility = "Individual / joint borrowers, tenant farmers, oral lessees & sharecroppers.",
                requiredDocuments = listOf("Filled KCC Application", "Identity & Address Proof", "Landholding Proof / Lease Agreement"),
                applicationProcess = "Submit application at any commercial bank, Regional Rural Bank (RRB), or Cooperative Bank.",
                isCentral = true,
                officialSource = "nabard.org / RBI",
                lastUpdated = "July 2026"
            )
        )

        schemes.add(
            GovtScheme(
                id = "soil_card",
                title = "Soil Health Card Scheme",
                shortDescription = "Free laboratory analysis of field soil nutrients (N, P, K, Organic Carbon, pH) with custom fertilizer recommendations.",
                benefits = "Free soil health card every 2-3 years to optimize fertilizer usage and boost soil productivity.",
                eligibility = "All farmers across India.",
                requiredDocuments = listOf("Aadhaar Card", "Field Location / Survey Number"),
                applicationProcess = "Contact local Block Development Office / Agriculture Field Officer for soil sampling.",
                isCentral = true,
                officialSource = "soilhealth.dac.gov.in",
                lastUpdated = "July 2026"
            )
        )

        // State specific scheme
        val currentState = if (state.isNotBlank()) state else "West Bengal"
        schemes.add(
            GovtScheme(
                id = "state_sub",
                title = "$currentState Krishi Bandhu / Farmers Support Scheme",
                shortDescription = "State government financial assistance and farm equipment subsidy program for agricultural promotion.",
                benefits = "Up to ₹10,000 per acre annual financial aid plus ₹2 Lakh life insurance benefit.",
                eligibility = "Farmers residing and owning agricultural land in $currentState.",
                requiredDocuments = listOf("Aadhaar", "Voter Card", "RoR / Khatian copy", "Bank Passbook"),
                applicationProcess = "Apply at Block Agriculture Office (BSK / Agriculture Officer).",
                isCentral = false,
                stateName = currentState,
                officialSource = "$currentState Department of Agriculture",
                lastUpdated = "August 2026"
            )
        )

        return schemes
    }
}
