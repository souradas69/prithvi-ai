package com.example.data

data class LocalizedTreatmentPlan(
    val immediateStepEn: String,
    val immediateStepHi: String,
    val immediateStepBn: String,
    val organicCureEn: String,
    val organicCureHi: String,
    val organicCureBn: String,
    val chemicalCureEn: String,
    val chemicalCureHi: String,
    val chemicalCureBn: String,
    val preventionEn: String,
    val preventionHi: String,
    val preventionBn: String
)

data class IndianCropDisease(
    val id: String,
    val cropNameEn: String,
    val cropNameHi: String,
    val cropNameBn: String,
    val diseaseNameEn: String,
    val diseaseNameHi: String,
    val diseaseNameBn: String,
    val scientificName: String,
    val pathogenCategory: String, // Fungal, Bacterial, Viral, Insect Pest
    val season: String, // Kharif, Rabi, Zaid, Year-round
    val affectedPart: String,
    val severityLevel: String, // Critical, Severe, Moderate, Mild
    val emoji: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val descriptionBn: String,
    val treatment: LocalizedTreatmentPlan
)

object IndianCropDiseaseRepository {
    val allDiseases: List<IndianCropDisease> = listOf(
        // 1. RICE - Bacterial Leaf Blight
        IndianCropDisease(
            id = "rice_blb",
            cropNameEn = "Rice / Paddy",
            cropNameHi = "धान",
            cropNameBn = "ধান",
            diseaseNameEn = "Bacterial Leaf Blight (BLB)",
            diseaseNameHi = "जीवाणु पत्ती झुलसा रोग",
            diseaseNameBn = "ব্যাকটেরিয়াল পাতা ব্লাইট",
            scientificName = "Xanthomonas oryzae pv. oryzae",
            pathogenCategory = "Bacterial Pathogen",
            season = "Kharif (Monsoon)",
            affectedPart = "Leaf margins and tips",
            severityLevel = "Severe",
            emoji = "🌾",
            descriptionEn = "One of the most destructive diseases in Indian paddy belts (Punjab, Haryana, West Bengal, Andhra Pradesh). Begins with wavy water-soaked yellowish-white stripes along leaf margins, causing leaves to turn straw-colored and wither rapidly (Kresek phase), drastically shrinking grain yield by up to 50%.",
            descriptionHi = "भारत के प्रमुख धान उत्पादक क्षेत्रों (पंजाब, हरियाणा, बंगाल, आंध्र प्रदेश) की अत्यंत घातक बीमारी। पत्तियों के किनारों पर लहरदार पीले-सफेद धारियां बनती हैं, जिससे पत्तियां सूखकर भूसे जैसी हो जाती हैं (क्रेसेक अवस्था)। इससे पैदावार में 30% से 50% तक भारी गिरावट आ सकती है।",
            descriptionBn = "ভারতের ধান প্রধান অঞ্চলের (পশ্চিমবঙ্গ, পাঞ্জাব, অন্ধ্র) মারাত্মক রোগ। পাতার কিনারা বরাবর ঢেউ খেলানো হলুদ-সাদা দাগ তৈরি হয়, যার ফলে পাতা খড়ের মতো শুকিয়ে যায় এবং ফলন ৩০-৫০% কমে যেতে পারে।",
            treatment = LocalizedTreatmentPlan(
                immediateStepEn = "1. Immediately drain standing water from field for 3-4 days to arrest bacterial spread.\n2. Stop all nitrogen/urea top-dressing until crop recovers.",
                immediateStepHi = "1. खेत से 3-4 दिनों के लिए जमा पानी निकाल दें ताकि बैक्टीरिया का फैलाव रुक सके।\n2. फसल ठीक होने तक यूरिया (नाइट्रोजन) का छिड़काव तुरंत बंद करें।",
                immediateStepBn = "1. ব্যাকটেরিয়ার বিস্তার থামাতে ক্ষেত থেকে ৩-৪ দিনের জন্য জমা জল বের করে দিন।\n2. ফসল সুস্থ না হওয়া পর্যন্ত ইউরিয়া সার দেওয়া বন্ধ রাখুন।",
                organicCureEn = "Foliar spray of 20% fresh cow dung slurry supernatant (filter through muslin cloth) or Pseudomonas fluorescens @ 5g/L water during early morning.",
                organicCureHi = "ताजे गोबर के घोल का निथरा हुआ पानी (20%) सूती कपड़े से छानकर या स्यूडोमोनास फ्लोरेसेंस (5 ग्राम प्रति लीटर पानी) सुबह के समय छिड़कें।",
                organicCureBn = "টাটকা গোবর গুলিয়ে কাপড়ে ছেঁকে নেওয়া পরিষ্কার জল (২০%) অথবা সিউডোমোনাস ফ্লুরোসেন্স (৫ গ্রাম প্রতি লিটার জল) সকালে স্প্রে করুন।",
                chemicalCureEn = "Spray Streptocycline (1g) + Copper Oxychloride 50% WP (25g) in 10 liters of water. Repeat spray after 10-12 days if disease persists.",
                chemicalCureHi = "स्ट्रेप्टोसाइक्लिन (1 ग्राम) + कॉपर ऑक्सीक्लोराइड 50% डब्ल्यूपी (25 ग्राम) को 10 लीटर पानी में मिलाकर छिड़कें। 10-12 दिन बाद आवश्यकतानुसार दोहराएं।",
                chemicalCureBn = "স্ট্রেপ্টোসাইক্লিন (১ গ্রাম) + কপার অক্সিক্লোরাইড ৫০% ডব্লিউপি (২৫ গ্রাম) প্রতি ১০ লিটার জলে গুলে স্প্রে করুন। ১০-১২ দিন পর আবার দিন।",
                preventionEn = "Sow certified resistant varieties (Swarna Sub1, IR-64, PR-126, Ajit). Treat seeds with Streptocycline (0.5g/kg seed). Maintain potash balance.",
                preventionHi = "रोगरोधी किस्में (स्वर्णा सब1, आईआर-64, पीआर-126) लगाएं। बुवाई से पहले बीज को स्ट्रेप्टोसाइक्लिन से उपचारित करें। पोटाश का उचित संतुलन रखें।",
                preventionBn = "প্রতিরোধী জাত (স্বর্ণ সাব১, আইআর-৬৪, পিআর-১২৬) চাষ করুন। বীজ শোধন করে রোপণ করুন ও জমিতে পরিমিত পটাশ দিন।"
            )
        ),

        // 2. RICE - Blast Disease
        IndianCropDisease(
            id = "rice_blast",
            cropNameEn = "Rice / Paddy",
            cropNameHi = "धान",
            cropNameBn = "ধান",
            diseaseNameEn = "Blast Disease (Neck & Leaf Blast)",
            diseaseNameHi = "झोंका रोग / ब्लास्ट रोग",
            diseaseNameBn = "ধানের ব্লাস্ট রোগ",
            scientificName = "Magnaporthe oryzae",
            pathogenCategory = "Fungal Pathogen",
            season = "Kharif & Late Rabi",
            affectedPart = "Leaves, Nodes, Neck of panicle",
            severityLevel = "Critical",
            emoji = "🌾",
            descriptionEn = "Characterized by spindle or diamond-shaped lesions with gray-white centers and brown margins on leaves. In neck blast, the node below the earhead rots black and breaks, leaving chaffy, empty grains.",
            descriptionHi = "पत्तियों पर आंख या नाव के आकार के धब्बे जिनके बीच का भाग राख जैसा धूसर और किनारे कत्थई होते हैं। गर्दन तोड़ (नेक ब्लास्ट) में बाली की गर्दन काली पड़कर टूट जाती है, जिससे दाना नहीं भरता और बालियां सफेद थोथी रह जाती हैं।",
            descriptionBn = "পাতায় নৌকার মতো বা চোখের আকৃতির দাগ দেখা যায় যার মাঝখান ধূসর ও কিনারা বাদামী। শীষ ব্লাস্টে শীষের গোড়া কালো হয়ে পচে ভেঙে পড়ে এবং দানা চিটে হয়ে যায়।",
            treatment = LocalizedTreatmentPlan(
                immediateStepEn = "Avoid late flooding and immediately withhold further chemical nitrogen application.",
                immediateStepHi = "खेत में नमी का संतुलन बनाए रखें और अधिक नाइट्रोजन/यूरिया देना तुरंत रोकें।",
                immediateStepBn = "জমিতে অতিরিক্ত নাইট্রোজেন সার দেওয়া বন্ধ করুন এবং সেচের ভারসাম্য রাখুন।",
                organicCureEn = "Spray Trichoderma harzianum @ 10g/L or fermented butter milk (sour chaas) mixed with copper vessel extract (50ml/L).",
                organicCureHi = "ट्राइकोडर्मा हरजिएनम (10 ग्राम/लीटर) या तांबे के बर्तन में रखी खट्टी छाछ (50 मिली/लीटर) पानी में मिलाकर छिड़कें।",
                organicCureBn = "ট্রাইকোডার্মা হারজিয়ানাম (১০ গ্রাম/লিটার) অথবা টক ঘোল (৫০ মিলি/লিটার জল) স্প্রে করুন।",
                chemicalCureEn = "Spray Tricyclazole 75% WP (Baan) @ 0.6g/L or Isoprothiolane 40% EC @ 1.5ml/L water at first sign of leaf spot or early panicle emergence.",
                chemicalCureHi = "ट्राइसाइक्लाजोल 75% डब्ल्यूपी (बान) 0.6 ग्राम प्रति लीटर पानी या आइसोप्रथियोलेन 40% ईसी 1.5 मिली प्रति लीटर पानी का छिड़काव करें।",
                chemicalCureBn = "ট্রাইসাইক্লাজোল ৭৫% ডব্লিউপি ০.৬ গ্রাম প্রতি লিটার জলে অথবা আইসোপ্রোথিওলেন ৪০% ইসি ১.৫ মিলি প্রতি লিটার জলে স্প্রে করুন।",
                preventionEn = "Treat seeds with Carbendazim 50% WP @ 2g/kg seed. Plant blast-tolerant hybrids like Pusa Basmati 1509 or MTU 1010.",
                preventionHi = "कार्बेंडाजिम 50% डब्ल्यूपी (2 ग्राम/किग्रा) से बीज उपचार करें। रोगरोधी किस्में (जैसे पूसा बासमती 1509, एमटीयू 1010) चुनें।",
                preventionBn = "বীজ শোধন করুন কার্বেনডাজিম দিয়ে। ব্লাস্ট প্রতিরোধী জাত যেমন পুসা বাসমতী বা এমটিইউ চাষ করুন।"
            )
        ),

        // 3. WHEAT - Yellow Stripe Rust
        IndianCropDisease(
            id = "wheat_yellow_rust",
            cropNameEn = "Wheat",
            cropNameHi = "गेहूं",
            cropNameBn = "গম",
            diseaseNameEn = "Yellow Stripe Rust",
            diseaseNameHi = "पीला रतुआ / पीला गेरुआ रोग",
            diseaseNameBn = "হলুদ মরিচা রোগ",
            scientificName = "Puccinia striiformis f. sp. tritici",
            pathogenCategory = "Fungal Rust",
            season = "Rabi (Winter)",
            affectedPart = "Leaf blades in parallel stripes",
            severityLevel = "Critical",
            emoji = "🌾",
            descriptionEn = "Prevalent in Northern Wheat Belt (Punjab, Haryana, Tarai region of Uttarakhand, Western UP). Appears as bright yellow, powdery pustules arranged in strictly linear rows resembling yellow stripes. Rubbing the leaf stains fingers yellow with spores.",
            descriptionHi = "उत्तर भारत (पंजाब, हरियाणा, तराई उत्तराखंड, पश्चिमी उत्तर प्रदेश) में गेहूं का भयंकर रोग। पत्तियों की नसों के समानांतर चमकीले पीले रंग की धारियां बनती हैं। छूने पर अंगुलियों पर हल्दी जैसा पीला चूर्ण लग जाता है।",
            descriptionBn = "উত্তর ভারতের গম চাষের অন্যতম ক্ষতিকারক রোগ। পাতার শিরার সমান্তরালে উজ্জ্বল হলুদ গুঁড়ো মতো রেখা তৈরি হয়। পাতায় হাত দিলে হলুদের মতো গুঁড়ো আঙুলে লেগে যায়।",
            treatment = LocalizedTreatmentPlan(
                immediateStepEn = "Survey wheat field borders facing prevailing northern winds; isolate and flag early focal infection spots.",
                immediateStepHi = "खेत के उत्तरी और पश्चिमी किनारों पर निगरानी करें और पीले धब्बे दिखते ही तुरंत स्प्रे की तैयारी करें।",
                immediateStepBn = "ক্ষেতের যে অংশে প্রথম হলুদ মরিচার আক্রমণ দেখা যায়, সেখানে অবিলম্বে ব্যবস্থা নিন।",
                organicCureEn = "Early stage dusting of micro-fine agricultural sulfur powder @ 20-25 kg/hectare during morning dew.",
                organicCureHi = "प्रारंभिक अवस्था में सुबह ओस के समय घुलनशील गंधक (सल्फर पाउडर) 25 किग्रा प्रति हेक्टेयर की दर से बुरकाव करें।",
                organicCureBn = "প্রাথমিক অবস্থায় সকালে ওসের সময় সালফার পাউডার ছিটিয়ে দেওয়া যেতে পারে।",
                chemicalCureEn = "Immediately spray Propiconazole 25% EC (Tilt) @ 1 ml/L (200 ml in 200 L water per acre) or Tebuconazole 25.9% EC @ 1 ml/L.",
                chemicalCureHi = "तुरंत प्रोपिकोनाजोल 25% ईसी (टिल्ट) 1 मिली प्रति लीटर पानी (200 मिली प्रति एकड़) या टेबुकोनाजोल 25.9% ईसी 1 मिली/लीटर का छिड़काव करें।",
                chemicalCureBn = "প্রপিকোনাজোল ২৫% ইসি (টিল্ট) প্রতি লিটার জলে ১ মিলি গুলে (একরে ২০০ মিলি) অবিলম্বে স্প্রে করুন।",
                preventionEn = "Sow stripe rust-resistant Indian wheat cultivars: DBW 187 (Karan Vandana), DBW 303, HD 3226, PBW 725. Avoid susceptible legacy varieties like HD 2967 in sub-mountainous zones.",
                preventionHi = "पीला रतुआ प्रतिरोधी किस्में लगाएं: डीबीडब्ल्यू 187, डीबीडब्ल्यू 303, एचडी 3226, पीबीडब्ल्यू 725। तराई क्षेत्रों में पुरानी संवेदनशील किस्में न बोएं।",
                preventionBn = "প্রতিরোধী জাত যেমন ডিবিডব্লিউ ১৮৭, ডিবিডব্লিউ ৩০৩, এইচডি ৩২২৬ চাষ করুন।"
            )
        ),

        // 4. TOMATO - Early Blight
        IndianCropDisease(
            id = "tomato_early_blight",
            cropNameEn = "Tomato",
            cropNameHi = "टमाटर",
            cropNameBn = "টমেটো",
            diseaseNameEn = "Early Blight",
            diseaseNameHi = "अगेती झुलसा रोग",
            diseaseNameBn = "আগাম ব্লাইট রোগ",
            scientificName = "Alternaria solani",
            pathogenCategory = "Fungal Pathogen",
            season = "All Seasons (Peak in warm humid months)",
            affectedPart = "Lower leaves, stems, fruit calyx",
            severityLevel = "Moderate",
            emoji = "🍅",
            descriptionEn = "Produces dark brown to black circular spots showing distinctive concentric rings ('target-board' or 'bullseye' appearance) surrounded by a chlorotic yellow halo. Causes lower leaf drop and collar rot on stems.",
            descriptionHi = "निचली पत्तियों पर गोल, भूरे-काले धब्बे बनते हैं जिनमें गोल छल्ले (टारगेट बोर्ड जैसे) स्पष्ट दिखाई देते हैं। धब्बों के चारों ओर पत्ती पीली पड़ जाती है। निचली पत्तियां सूखकर गिर जाती हैं और तने पर भी घाव बन जाते हैं।",
            descriptionBn = "পাতায় গোল গাঢ় বাদামী দাগ তৈরি হয় যার মধ্যে বলয়ের মতো রিং দেখা যায় (টার্গেট বোর্ড)। দাগের চারপাশে হলুদ আভা থাকে। নিচের পাতা ঝরে পড়ে ও ফলন ক্ষতিগ্রস্ত হয়।",
            treatment = LocalizedTreatmentPlan(
                immediateStepEn = "Prune and dispose of lowest infected leaves touching soil; stake tomato plants to elevate foliage off ground.",
                immediateStepHi = "जमीन से छूने वाली निचली बीमार पत्तियों को तोड़कर नष्ट कर दें। पौधों को बांस-सुतली से सहारा (स्टेकिंग) दें।",
                immediateStepBn = "মাটি সংলগ্ন আক্রান্ত পাতাগুলো ছিঁড়ে পুড়িয়ে ফেলুন এবং গাছকে বাঁশের খুঁটি দিয়ে বেঁধে খাড়া রাখুন।",
                organicCureEn = "Foliar spray of 5% Neem Seed Kernel Extract (NSKE) or cold-pressed Neem Oil (5ml/L water) mixed with soap surfactant.",
                organicCureHi = "नीम के बीज की गिरी का अर्क (NSKE 5%) या नीम का तेल (5 मिली प्रति लीटर पानी) थोड़े साबुन के घोल के साथ मिलाकर छिड़कें।",
                organicCureBn = "নিম তেলের স্প্রে (৫ মিলি প্রতি লিটার জল সামান্য শ্যাম্পু বা সাবান গুলে) সপ্তাহে একবার স্প্রে করুন।",
                chemicalCureEn = "Spray Mancozeb 75% WP @ 2.5g/L or Copper Oxychloride 50% WP @ 3g/L. In advanced cases, apply Chlorothalonil 75% WP @ 2g/L.",
                chemicalCureHi = "मेंकोजेब 75% डब्ल्यूपी (2.5 ग्राम/लीटर) या कॉपर ऑक्सीक्लोराइड 50% डब्ल्यूपी (3 ग्राम/लीटर) पानी में मिलाकर 10-12 दिन के अंतर पर छिड़कें।",
                chemicalCureBn = "ম্যানকোজেব ৭৫% ডব্লিউপি (২.৫ গ্রাম/লিটার) অথবা কপার অক্সিক্লোরাইড (৩ গ্রাম/লিটার) স্প্রে করুন।",
                preventionEn = "Maintain 60cm row spacing for good air circulation; use drip irrigation rather than overhead sprinklers to prevent leaf moisture.",
                preventionHi = "पौधों के बीच 60 सेमी की दूरी रखें। फव्वारा सिंचाई की जगह टपक (ड्रिप) सिंचाई अपनाएं ताकि पत्तियों पर पानी न पड़े।",
                preventionBn = "গাছের মধ্যে পর্যাপ্ত ফাঁকা জায়গা রাখুন। ওপর থেকে জল না দিয়ে গোড়ায় জল দিন যাতে পাতা ভিজে না থাকে।"
            )
        ),

        // 5. TOMATO - Leaf Curl Virus
        IndianCropDisease(
            id = "tomato_leaf_curl",
            cropNameEn = "Tomato",
            cropNameHi = "टमाटर",
            cropNameBn = "টমেটো",
            diseaseNameEn = "Tomato Leaf Curl Virus (ToLCV)",
            diseaseNameHi = "पर्ण कुंचन / चुर्रा-मुर्रा रोग",
            diseaseNameBn = "টমেটোর পাতা কোঁকড়ানো রোগ",
            scientificName = "Tomato leaf curl virus (Begomovirus)",
            pathogenCategory = "Viral Pathogen (Whitefly vector)",
            season = "Kharif & Zaid (Summer)",
            affectedPart = "Apical young leaves, shoots, flowers",
            severityLevel = "Severe",
            emoji = "🍅",
            descriptionEn = "Transmitted by the whitefly (Bemisia tabaci). Symptoms include pronounced upward curling, puckering, reduction in leaf size, thick rubbery foliage, and stunted bushy plant growth with flower drop.",
            descriptionHi = "यह रोग सफेद मक्खी (व्हाइटफ्लाई) कीट द्वारा फैलता है। नई पत्तियां ऊपर की ओर मुड़ जाती हैं, छोटी व खुरदरी हो जाती हैं। पौधा बौना व झाड़ीनुमा हो जाता है तथा फूल व फल नहीं बनते।",
            descriptionBn = "সাদা মাছি দ্বারা বাহিত ভাইরাস রোগ। কচি পাতাগুলো ওপরের দিকে কুঁকড়ে যায়, পাতার আকার ছোট হয়ে যায় এবং গাছ খর্বাকৃতি ঝোপের মতো হয়ে পড়ে ও ফুল ঝরে যায়।",
            treatment = LocalizedTreatmentPlan(
                immediateStepEn = "Rogue out (uproot) severely stunted diseased plants in early nursery/field stage and bury them away from field.",
                immediateStepHi = "रोगग्रस्त शुरुआती पौधों को तुरंत उखाड़कर खेत से दूर गड्ढे में दबा दें।",
                immediateStepBn = "আক্রান্ত গাছগুলোকে গোড়া সহ উপড়ে ক্ষেত থেকে দূরে মাটিতে পুঁতে ফেলুন।",
                organicCureEn = "Install bright yellow sticky traps (15-20 traps/acre) to catch adult whiteflies. Spray Neem oil (5ml/L) or Verticillium lecanii (5g/L).",
                organicCureHi = "सफेद मक्खी पकड़ने के लिए पीले चिपचिपे कार्ड (येलो स्टिकी ट्रैप 15-20 प्रति एकड़) लगाएं। नीम तेल (5 मिली/लीटर) का छिड़काव करें।",
                organicCureBn = "ক্ষেতে হলুদ আঠালো ফাঁদ (একর প্রতি ১৫-২০টি) টাঙান সাদা মাছি মারার জন্য এবং নিম তেল স্প্রে করুন।",
                chemicalCureEn = "Spray Acetamiprid 20% SP @ 0.5g/L or Diafenthiuron 50% WP @ 1.2g/L or Flonicamid 50% WG @ 0.3g/L to control vector whiteflies.",
                chemicalCureHi = "सफेद मक्खी नियंत्रण हेतु एसिटामिप्रिड 20% एसपी 0.5 ग्राम/लीटर या डायफेन्थियूरॉन 50% डब्ल्यूपी 1.2 ग्राम/लीटर का छिड़काव करें।",
                chemicalCureBn = "অ্যাসিটামিপ্রিড ২০% এসপি ০.৫ গ্রাম প্রতি লিটার অথবা ডায়াফেন্থিউরন ১.২ গ্রাম প্রতি লিটার জলে গুলে স্প্রে করুন।",
                preventionEn = "Raise tomato seedlings under 40-mesh insect-proof nylon net. Grow resistant varieties like Pusa Rohini, Kashi Aman, Arka Rakshak.",
                preventionHi = "नर्सरी में 40-मेश की मच्छरदानी जाली लगाकर पौधे तैयार करें। प्रतिरोधी किस्में (अर्का रक्षक, काशी अमन, पूसा रोहिणी) लगाएं।",
                preventionBn = "জাল দিয়ে ঢাকা নার্সারিতে চারা তৈরি করুন। প্রতিরোধী জাত যেমন অর্কা রক্ষক চাষ করুন।"
            )
        ),

        // 6. POTATO - Late Blight
        IndianCropDisease(
            id = "potato_late_blight",
            cropNameEn = "Potato",
            cropNameHi = "आलू",
            cropNameBn = "আলু",
            diseaseNameEn = "Late Blight",
            diseaseNameHi = "पछेती झुलसा रोग (आलू का महामारी रोग)",
            diseaseNameBn = "আলুর নাবী ধসা / লেইট ব্লাইট",
            scientificName = "Phytophthora infestans",
            pathogenCategory = "Oomycete / Fungal-like",
            season = "Rabi (Winter - Dec to Feb)",
            affectedPart = "Leaves, petioles, stems, tubers",
            severityLevel = "Critical",
            emoji = "🥔",
            descriptionEn = "The notorious Irish famine pathogen responsible for wiping out potato fields across UP, Bihar, Punjab and Bengal in cloudy, foggy winter spells (temp 12-20°C, RH >85%). Water-soaked dark lesions with white cottony mildew on undersides turn entire fields black within 48 hours.",
            descriptionHi = "आलू का सबसे विनाशकारी रोग। कोहरे, बादलों और 12-20°C तापमान में तेजी से फैलता है। पत्तियों पर जलभरे काले-भूरे धब्बे बनते हैं जिनके नीचे सफेद रुई जैसी फफूंद दिखती है। मात्र 48 घंटे में पूरा खेत झुलसकर काला पड़ जाता है और सड़ी हुई दुर्गंध आती है।",
            descriptionBn = "আলু চাষের সবচেয়ে ভয়াবহ রোগ। কুয়াশাচ্ছন্ন ও মেঘলা আবহাওয়ায় (১২-২০°C তাপমাত্রা) পাতার তলায় সাদা ছাতা দেখা যায় এবং ২ দিনের মধ্যে পুরো ক্ষেত পুড়ে যাওয়ার মতো কালো হয়ে যায়।",
            treatment = LocalizedTreatmentPlan(
                immediateStepEn = "Cease all irrigation immediately. Cut and burn tops (haulms) 10 days before harvest if late blight strikes late in season.",
                immediateStepHi = "खेत में तुरंत सिंचाई बंद करें। यदि फसल पकने के करीब हो तो पौधों के ऊपरी भाग (डंठल) काटकर नष्ट कर दें ताकि आलू में संक्रमण न पहुंचे।",
                immediateStepBn = "অবিলম্বে সেচ বন্ধ করুন। আলু তোলার ১০ দিন আগে আক্রান্ত গাছের ডগা কেটে সরিয়ে ফেলুন যাতে কন্দ পচে না যায়।",
                organicCureEn = "Preventative spray of Bordeaux mixture (1%) or Copper Hydroxide (2.5g/L). Enrich soil with Trichoderma viride.",
                organicCureHi = "रोग आने से पहले बोर्डो मिश्रण (1%) या कॉपर हाइड्रॉक्साइड (2.5 ग्राम/लीटर) का छिड़काव करें। ट्राइकोडर्मा विरिडी का प्रयोग करें।",
                organicCureBn = "আক্রমণের পূর্বে বোর্দো মিশ্রণ (১%) অথবা কপার হাইড্রোক্সাইড স্প্রে করুন।",
                chemicalCureEn = "Prophylactic: Mancozeb 75% WP @ 2.5g/L. Curative upon first lesion: Cymoxanil 8% + Mancozeb 64% WP (Curzate @ 2.5g/L) or Dimethomorph 50% WP @ 1g/L.",
                chemicalCureHi = "रोग लक्षण दिखते ही साइमोक्सानिल 8% + मेंकोजेब 64% डब्ल्यूपी (कर्जेट 2.5 ग्राम/लीटर) या डाईमेथोमॉर्फ 50% डब्ल्यूपी (1 ग्राम/लीटर) का तुरंत छिड़काव करें।",
                chemicalCureBn = "লক্ষণ দেখা মাত্রই সাইমোক্সানিল ৮% + ম্যানকোজেব ৬৪% (কারজেট ২.৫ গ্রাম/লিটার) অথবা মেটালাক্সিল গুলে স্প্রে করুন।",
                preventionEn = "Plant certified disease-free tubers (Kufri Jyoti, Kufri Pukhraj, Kufri Mohan). High earthing up (ridging) prevents spores from washing into soil.",
                preventionHi = "प्रमाणित रोगमुक्त बीज (कुफरी ज्योति, कुफरी पुखराज, कुफरी मोहन) लगाएं। मेड़ों पर 8-10 इंच ऊंची मिट्टी चढ़ाएं।",
                preventionBn = "ভালো মানের বীজ আলু (কুফরি জ্যোতি, কুফরি পুখরাজ) রোপণ করুন এবং গোড়ায় ভালো করে মাটি তুলে দিন।"
            )
        ),

        // 7. COTTON - Leaf Curl Virus & Whitefly
        IndianCropDisease(
            id = "cotton_leaf_curl",
            cropNameEn = "Cotton",
            cropNameHi = "कपास",
            cropNameBn = "তুলা",
            diseaseNameEn = "Cotton Leaf Curl Virus (CLCuV)",
            diseaseNameHi = "कपास का पत्ती मरोड़ रोग (पत्ती मुड़ना)",
            diseaseNameBn = "তুলার পাতা কোঁকড়ানো রোগ",
            scientificName = "Cotton leaf curl virus (CLCuV)",
            pathogenCategory = "Viral Pathogen",
            season = "Kharif",
            affectedPart = "Leaves, veins, shoots",
            severityLevel = "Severe",
            emoji = "🌱",
            descriptionEn = "Severe threat in North Cotton Zone (Punjab, Haryana, Rajasthan). Veins thicken and become dark green, leaves curl upward in cup shapes, and peculiar leaf-like outgrowths (enations) develop on lower vein surfaces.",
            descriptionHi = "उत्तरी कपास बेल्ट (पंजाब, हरियाणा, राजस्थान) का मुख्य संकट। पत्तियों की नसें मोटी होकर उभर आती हैं, पत्तियां प्याले के आकार में ऊपर मुड़ जाती हैं और पत्ती की निचली सतह पर छोटी-छोटी पत्तीनुमा संरचनाएं बन जाती हैं।",
            descriptionBn = "উত্তর ভারতের তুলা চাষের প্রধান রোগ। পাতার শিরাগুলো মোটা হয়ে যায়, পাতা পেয়ালার মতো বেঁকে যায় এবং নিচে অতিরিক্ত পাতার মতো অংশ জন্মায়।",
            treatment = LocalizedTreatmentPlan(
                immediateStepEn = "Eradicate alternate weed hosts (Parthenium, Abutilon indicum, Xanthium) growing on canal banks and field bunds.",
                immediateStepHi = "खेत के मेड़ों, नालियों और किनारों से गाजरघास व कंघी जैसे खरपतवार तुरंत उखाड़कर नष्ट करें।",
                immediateStepBn = "ক্ষেতের আইল ও আশপাশের আগাছাগুলো উপড়ে পুড়িয়ে ফেলুন।",
                organicCureEn = "Spray Fish Oil Rosin Soap (25g/L) or 5% Neem Seed Kernel Extract (NSKE) during morning hours against whitefly vectors.",
                organicCureHi = "सफेद मक्खी के लिए फिश ऑयल रोजिन सोप (25 ग्राम/लीटर) या नीम अर्क (5%) का छिड़काव करें।",
                organicCureBn = "সাদা মাছির দমনে নিম বীজের নির্যাস (৫%) অথবা নিম তেল স্প্রে করুন।",
                chemicalCureEn = "Spray Pyriproxyfen 10% EC @ 2 ml/L or Diafenthiuron 50% WP @ 1.2 g/L or Afidopyropen 50 g/L @ 2 ml/L targeting lower leaf surfaces.",
                chemicalCureHi = "पत्तियों की निचली सतह पर पाइरीप्रॉक्सीफेन 10% ईसी (2 मिली/लीटर) या डायफेन्थियूरॉन 50% डब्ल्यूपी (1.2 ग्राम/लीटर) का छिड़काव करें।",
                chemicalCureBn = "পাতার নিচে ডায়াফেন্থিউরন ১.২ গ্রাম প্রতি লিটার অথবা পাইরিপ্রক্সিফেন ২ মিলি প্রতি লিটার জলে স্প্রে করুন।",
                preventionEn = "Adopt recommended resistant Bt cotton hybrids. Avoid synthetic pyrethroid overuse which induces whitefly resurgence.",
                preventionHi = "रोगरोधी बीटी कपास संकर किस्में लगाएं। सिंथेटिक कीटनाशकों का अंधाधुंध प्रयोग न करें।",
                preventionBn = "প্রতিরোধী বিটি তুলার জাত চাষ করুন এবং মাত্রাতিরিক্ত রাসায়নিক কীটনাশক প্রয়োগ এড়িয়ে চলুন।"
            )
        ),

        // 8. MUSTARD - White Rust
        IndianCropDisease(
            id = "mustard_white_rust",
            cropNameEn = "Mustard / Rapeseed",
            cropNameHi = "सरसों / राई",
            cropNameBn = "সর্ষে",
            diseaseNameEn = "White Rust (Blister Smut)",
            diseaseNameHi = "सफेद रतुआ रोग (छाला रोग)",
            diseaseNameBn = "সর্ষের সাদা মরিচা রোগ",
            scientificName = "Albugo candida",
            pathogenCategory = "Oomycete Fungus",
            season = "Rabi (Winter)",
            affectedPart = "Leaves, stems, floral inflorescence ('staghead')",
            severityLevel = "Severe",
            emoji = "🌻",
            descriptionEn = "Produces prominent white to creamy-yellow raised pustules/blisters on the underside of mustard leaves. When infection enters the flower heads, it causes monstrous thickening and twisting called 'Staghead', producing zero oilseed yield.",
            descriptionHi = "सरसों की पत्तियों के निचले हिस्से पर सफेद या हल्के पीले रंग के उभरे हुए फफोले (छाले) बनते हैं। फूलों की बालियों में संक्रमण होने पर फूल मोटे, विकृत होकर बारहसिंगे के सींग जैसे (स्टैगहेड) हो जाते हैं, जिससे बीज नहीं बनता।",
            descriptionBn = "সর্ষে পাতার নিচের অংশে সাদা বা ঘিয়ে রঙের ফোসকার মতো দাগ দেখা যায়। ফুলের মঞ্জরিতে সংক্রমণ হলে ফুল বিকৃত হয়ে হরিণের শিং-এর মতো আকার নেয় এবং বীজ তৈরি হয় না।",
            treatment = LocalizedTreatmentPlan(
                immediateStepEn = "Clip and destroy staghead malformed inflorescences as soon as they appear in late January.",
                immediateStepHi = "फूलों में बारहसिंगा जैसे विकृत भाग (स्टैगहेड) दिखते ही उन्हें काटकर जला दें।",
                immediateStepBn = "ফুল বিকৃত হতে শুরু করলেই সেই অংশগুলো কেটে পুড়িয়ে ফেলুন।",
                organicCureEn = "Foliar spray of 2% cow urine (Gomutra) solution with dissolved garlic extract, or Trichoderma viride @ 5g/L.",
                organicCureHi = "देशी गाय के गोमूत्र (2-3%) में लहसुन का अर्क मिलाकर छिड़कें, या ट्राइकोडर्मा विरिडी (5 ग्राम/लीटर) दें।",
                organicCureBn = "গোমূত্র ও রসুনের নির্যাস মিশ্রিত জল অথবা ট্রাইকোডার্মা স্প্রে করুন।",
                chemicalCureEn = "Spray Metalaxyl 8% + Mancozeb 64% WP (Ridomil MZ @ 2g/L) or Mancozeb 75% WP @ 2.5g/L at 45 and 60 days after sowing.",
                chemicalCureHi = "बुवाई के 45 व 60 दिन बाद मेंकोजेब 75% डब्ल्यूपी (2.5 ग्राम/लीटर) या रिडोमिल एमजेड (2 ग्राम/लीटर) का छिड़काव करें।",
                chemicalCureBn = "মেটালাক্সিল + ম্যানকোজেব (রিডোমিল ২ গ্রাম/লিটার) অথবা ম্যানকোজেব ২.৫ গ্রাম প্রতি লিটার জলে গুলে স্প্রে করুন।",
                preventionEn = "Sow mustard early (before 20th October) to escape heavy disease pressure. Treat seed with Apron 35 SD @ 6g/kg seed.",
                preventionHi = "अगेती बुवाई (20 अक्टूबर से पहले) करें ताकि बीमारी का प्रकोप न हो। बीज को एप्रॉन 35 एसडी से उपचारित करें।",
                preventionBn = "অক্টোবরের মাঝামাঝি সময়ে আগাম সর্ষে বপন করুন এবং বীজ শোধন করে রোপণ করুন।"
            )
        ),

        // 9. SUGARCANE - Red Rot
        IndianCropDisease(
            id = "sugarcane_red_rot",
            cropNameEn = "Sugarcane",
            cropNameHi = "गन्ना",
            cropNameBn = "আখ",
            diseaseNameEn = "Red Rot (Cancer of Sugarcane)",
            diseaseNameHi = "लाल सड़न रोग (गन्ने का कैंसर)",
            diseaseNameBn = "আখের লাল পচা রোগ (আখের ক্যান্সার)",
            scientificName = "Colletotrichum falcatum",
            pathogenCategory = "Fungal Pathogen",
            season = "Year-round (Severe in rainy season)",
            affectedPart = "Pith of stalk, leaf midrib",
            severityLevel = "Critical",
            emoji = "🎋",
            descriptionEn = "Referred to as the 'Cancer of Sugarcane' in Western UP, Bihar, and Maharashtra. Third or fourth leaf from top shows yellowing and withering. Splitting open the stalk reveals blood-red pith tissue interrupted with characteristic white transverse patches and an alcoholic / sour fermentation smell.",
            descriptionHi = "गन्ने का सबसे विनाशकारी रोग, जिसे 'गन्ने का कैंसर' भी कहते हैं। ऊपर से तीसरी-चौथी पत्ती पीली पड़कर सूखने लगती है। गन्ने को बीच से चीरने पर अंदर का गूदा लाल दिखाई देता है जिसमें बीच-बीच में सफेद चौड़े धब्बे होते हैं और सिरके/शराब जैसी खट्टी बदबू आती है।",
            descriptionBn = "আখের মারাত্মক ক্ষতিকারক রোগ। ওপরের ৩-৪ নম্বর পাতা হলুদ হয়ে শুকিয়ে যায়। আখ লম্বালম্বি কাটলে ভেতরে লাল রঙের আঁশ দেখা যায় যাতে আড়াআড়ি সাদা দাগ থাকে এবং পচা অ্যালকোহলের গন্ধ বের হয়।",
            treatment = LocalizedTreatmentPlan(
                immediateStepEn = "Immediately rogue out diseased clumps with roots, burn them, and disinfect the pit with lime (Chuna) powder.",
                immediateStepHi = "संक्रमित गन्ने के थूहे को जड़ सहित उखाड़कर जला दें और उस जगह पर चूने का पाउडर छिड़कें।",
                immediateStepBn = "আক্রান্ত গাছ শিকড় সহ উপড়ে পুড়িয়ে ফেলুন এবং গর্তে চুন গুঁড়ো ছড়িয়ে দিন।",
                organicCureEn = "Dip seed setts in Trichoderma harzianum slurry (10g/L) for 30 minutes before planting. Apply enriched neem cake in soil.",
                organicCureHi = "बुवाई से पहले गन्ने के टुकड़ों को ट्राइकोडर्मा घोल (10 ग्राम/लीटर) में 30 मिनट डुबोएं। खेत में नीम की खली डालें।",
                organicCureBn = "রোপণের আগে ট্রাইকোডার্মা গোলা জলে আখের টুকরোগুলো ৩০ মিনিট ভিজিয়ে রাখুন।",
                chemicalCureEn = "Setts treatment: Dip 2-bud setts in Carbendazim 50% WP (1g/L) or Thiophanate Methyl 70% WP (1.5g/L) for 15 minutes before sowing.",
                chemicalCureHi = "बीज शोधन: बुवाई से पहले 2-आंख वाले टुकड़ों को कार्बेंडाजिम 50% डब्ल्यूपी (1 ग्राम/लीटर) घोल में 15 मिनट डुबोकर उपचारित करें।",
                chemicalCureBn = "আখের টুকরো কার্বেনডাজিম ১ গ্রাম প্রতি লিটার জলে ১৫ মিনিট ভিজিয়ে শোধন করে রোপণ করুন।",
                preventionEn = "Never use seed cane from red rot-infected plots. Practice 2-year crop rotation with paddy or green manure. Plant resistant varieties like Co 0238 (with strict health monitoring), CoLk 94184, CoPant 84211.",
                preventionHi = "रोगी खेत से बीज कभी न लें। धान या ढैंचा के साथ फसल चक्र अपनाएं। रोगरोधी किस्में लगाएं।",
                preventionBn = "আক্রান্ত ক্ষেতের আখ বীজের জন্য ব্যবহার করবেন না। ধানের সাথে ফসল চক্র মেনে চাষ করুন।"
            )
        ),

        // 10. CHILLI - Anthracnose & Dieback
        IndianCropDisease(
            id = "chilli_anthracnose",
            cropNameEn = "Chilli / Pepper",
            cropNameHi = "मिर्च",
            cropNameBn = "লঙ্কা",
            diseaseNameEn = "Anthracnose & Fruit Rot (Dieback)",
            diseaseNameHi = "डाईबैक व फल सड़न रोग",
            diseaseNameBn = "লঙ্কার ডাইব্যাক ও ফল পচা রোগ",
            scientificName = "Colletotrichum capsici",
            pathogenCategory = "Fungal Pathogen",
            season = "Kharif & Post-Monsoon",
            affectedPart = "Shoots, twigs, ripe and green fruits",
            severityLevel = "Severe",
            emoji = "🌶️",
            descriptionEn = "Dieback causes tender twigs to dry and wither backwards from top downwards, turning white/bleached. On ripe chillies, it causes sunken circular spots with black concentric rings of fruiting bodies, turning fruits straw-colored and unmarketable.",
            descriptionHi = "पौधे की ऊपरी शाखाएं सिरे से नीचे की ओर सूखने लगती हैं और भूसे जैसी सफेद हो जाती हैं (डाईबैक)। पकी मिर्चों पर गोल धंसे हुए काले धब्बे बनते हैं और मिर्च समय से पहले सूखकर सफेद-पीली हो जाती है, जिससे भारी नुकसान होता है।",
            descriptionBn = "গাছের ওপরের ডালপালা ওপর থেকে নিচের দিকে শুকিয়ে সাদা হয়ে যায়। পাকা লঙ্কায় গোল বসে যাওয়া কালো দাগ দেখা যায় এবং লঙ্কা শুকিয়ে নষ্ট হয়ে যায়।",
            treatment = LocalizedTreatmentPlan(
                immediateStepEn = "Prune dried twigs 2 inches below healthy green tissue and safely discard infected fruits.",
                immediateStepHi = "सूखी हुई टहनियों को स्वस्थ भाग से 2 इंच नीचे से काटकर जला दें और सड़ी मिर्चें खेत से हटाएं।",
                immediateStepBn = "শুকিয়ে যাওয়া ডালের কিছুটা ভালো অংশ সহ কেটে ফেলে দিন এবং পচা লঙ্কা সংগ্রহ করে নষ্ট করুন।",
                organicCureEn = "Foliar spray of fermented sour buttermilk (Khatti Chaas 50ml/L) or Trichoderma viride @ 5g/L water.",
                organicCureHi = "खट्टी छाछ (50 मिली प्रति लीटर पानी) या ट्राइकोडर्मा विरिडी (5 ग्राम/लीटर) का छिड़काव करें।",
                organicCureBn = "টক ঘোল ৫০ মিলি প্রতি লিটার জলে মিশিয়ে অথবা ট্রাইকোডার্মা স্প্রে করুন।",
                chemicalCureEn = "Spray Azoxystrobin 23% SC @ 1 ml/L or Difenoconazole 25% EC @ 0.5 ml/L or Tebuconazole 50% + Trifloxystrobin 25% WG (Nativo @ 0.6 g/L).",
                chemicalCureHi = "एजोक्सीस्ट्रोबिन 23% एससी (1 मिली/लीटर) या टेबुकोनाजोल + ट्राइफ्लोक्सीस्ट्रोबिन (नेटिवो 0.6 ग्राम/लीटर) का छिड़काव करें।",
                chemicalCureBn = "অ্যাজোক্সিস্ট্রোবিন ১ মিলি প্রতি লিটার অথবা নেটিভো ০.৬ গ্রাম প্রতি লিটার জলে গুলে স্প্রে করুন।",
                preventionEn = "Treat seeds with Thiram or Captan @ 3g/kg seed. Harvest ripe fruits promptly; avoid overhead irrigation during flowering.",
                preventionHi = "थिरम (3 ग्राम/किग्रा) से बीज उपचार करें। पकी मिर्च की तुड़ाई में देरी न करें। फव्वारा सिंचाई से बचें।",
                preventionBn = "বীজ শোধন করুন এবং লঙ্কা পাকার সাথে সাথে তুলে ফেলুন। ওপর থেকে জল ছিটাবেন না।"
            )
        ),

        // 11. CHICKPEA - Fusarium Wilt
        IndianCropDisease(
            id = "chickpea_wilt",
            cropNameEn = "Chickpea / Bengal Gram",
            cropNameHi = "चना",
            cropNameBn = "ছোলা",
            diseaseNameEn = "Fusarium Wilt (Ukhta Rog)",
            diseaseNameHi = "उकठा रोग (चना सूखा)",
            diseaseNameBn = "ছোলার উইল্ট বা ঢলে পড়া রোগ",
            scientificName = "Fusarium oxysporum f. sp. ciceris",
            pathogenCategory = "Soil-borne Fungus",
            season = "Rabi (Winter)",
            affectedPart = "Roots, vascular xylem bundles",
            severityLevel = "Critical",
            emoji = "🌱",
            descriptionEn = "Soil-borne wilt attacking chickpea fields across MP, Maharashtra, Rajasthan. Plants droop, leaves turn dull olive green then brown. Splitting taproot vertically reveals deep dark-brown vascular xylem discoloration inside the root core.",
            descriptionHi = "चने का सबसे मुख्य रोग (मध्य प्रदेश, महाराष्ट्र, राजस्थान में व्यापक)। पौधे अचानक मुरझाकर सूख जाते हैं। पत्तियां पहले फीकी हरी और फिर भूरी हो जाती हैं। मुख्य जड़ को लंबाई में चीरने पर अंदर की नलिकाओं में भूरी या काली लकीर दिखाई देती है।",
            descriptionBn = "মাটিবাহিত ছত্রাক রোগ। গাছ হঠাৎ ঝিমিয়ে পড়ে এবং শুকিয়ে যায়। মূল লম্বালম্বি কাটলে ভেতরে গাঢ় বাদামী দাগ বা শিরার বিবর্ণতা স্পষ্ট দেখা যায়।",
            treatment = LocalizedTreatmentPlan(
                immediateStepEn = "Deep summer ploughing to expose dormant chlamydospores to scorching May-June sun.",
                immediateStepHi = "मई-जून के महीने में खेत की गहरी जुताई करें ताकि तेज धूप से मिट्टी में मौजूद फफूंद नष्ट हो जाए।",
                immediateStepBn = "গ্রীষ্মকালে জমি গভীরভাবে চাষ দিয়ে রোদে ফেলে রাখুন যাতে মাটির মধ্যকার ছত্রাক নষ্ট হয়।",
                organicCureEn = "Apply Trichoderma viride enriched farmyard manure (FYM @ 2.5 kg Trichoderma mixed in 100 kg cow dung manure per acre) at final land prep.",
                organicCureHi = "अंतिम जुताई के समय गोबर की खाद में मिला ट्राइकोडर्मा विरिडी (2.5 किग्रा प्रति 100 किग्रा गोबर खाद प्रति एकड़) मिट्टी में मिलाएं।",
                organicCureBn = "জমি তৈরির সময় ট্রাইকোডার্মা মেশানো গোবর সার জমিতে ভালোভাবে প্রয়োগ করুন।",
                chemicalCureEn = "Seed treatment with Carbendazim 12% + Mancozeb 63% WP (Saaf @ 2.5g/kg seed) or Carboxin 37.5% + Thiram 37.5% DS (Vitavax Power @ 2g/kg).",
                chemicalCureHi = "बीज शोधन: कार्बाेंडाजिम + मेंकोजेब (साफ 2.5 ग्राम/किग्रा बीज) या वीटावैक्स पावर (2 ग्राम/किग्रा) से उपचारित करके ही बोएं।",
                chemicalCureBn = "বীজ শোধন: কার্বেনডাজিম + ম্যানকোজেব (সাফ ২.৫ গ্রাম প্রতি কেজি বীজ) দিয়ে শোধন করে বপন করুন।",
                preventionEn = "Plant wilt-resistant chickpea varieties like JG 11, JG 16, JG 130, JAKI 9218, RVG 202, RVG 203. Practice 3-year crop rotation with sorghum or wheat.",
                preventionHi = "उकठा प्रतिरोधी किस्में लगाएं: जेजी 11, जेजी 16, जेजी 130, जाकी 9218, आरवीजी 202, आरवीजी 203। ज्वार या गेहूं के साथ फसल चक्र अपनाएं।",
                preventionBn = "উইল্ট প্রতিরোধী জাত যেমন জেজি ১১, জাকি ৯২১৮ চাষ করুন এবং তিন বছর পর পর ফসল বদল করুন।"
            )
        ),

        // 12. MAIZE - Fall Armyworm
        IndianCropDisease(
            id = "maize_fall_armyworm",
            cropNameEn = "Maize / Corn",
            cropNameHi = "मक्का",
            cropNameBn = "ভুট্টা",
            diseaseNameEn = "Fall Armyworm (FAW)",
            diseaseNameHi = "फॉल आर्मीवर्म (सैनिक कीट)",
            diseaseNameBn = "ফল আর্মিওয়ার্ম পোকা",
            scientificName = "Spodoptera frugiperda",
            pathogenCategory = "Invasive Insect Pest",
            season = "Kharif & Rabi",
            affectedPart = "Whorl, young leaves, tassel, cobs",
            severityLevel = "Critical",
            emoji = "🌽",
            descriptionEn = "Devastating invasive caterpillar feeding inside maize whorls. Leaves show characteristic elongated 'window-pane' feeding patches and large ragged holes. Heavy sawdust-like brownish frass packs the central whorl funnel.",
            descriptionHi = "मक्का की फसल का आक्रामक कीट। पत्तियां छलनी हो जाती हैं और उन पर खिड़की जैसी झिल्लियां व बड़े छेद बन जाते हैं। पौधे की बीच की पोंगो (गोभ) में लकड़ी के बुरादे जैसा मल भरा रहता है। समय पर न रोकें तो पूरी फसल बर्बाद हो जाती है।",
            descriptionBn = "ভুট্টার মারাত্মক ক্ষতিকারক শুঁয়োপোকা। কচি পাতার মাঝখানে করাত দিয়ে কাটার মতো ও জানালার মতো দাগ দেখা যায় এবং গাছের কেন্দ্রে কাঠের গুঁড়োর মতো মল জমে থাকে।",
            treatment = LocalizedTreatmentPlan(
                immediateStepEn = "Handpick egg masses covered with fuzzy cream scales and early larvae in morning hours from smallholdings.",
                immediateStepHi = "सुबह के समय पत्तियों पर लगे रोएंदार अंडों के गुच्छे और छोटी इल्लियों को हाथ से चुनकर नष्ट करें।",
                immediateStepBn = "সকালে পাতার ওপর জমা ডিমের গাদা ও কচি শুঁয়োপোকা হাত দিয়ে সংগ্রহ করে ধ্বংস করুন।",
                organicCureEn = "Drop a pinch of dry soil/ash mixed with neem seed powder (9:1 ratio) directly into the whorl. Spray Bacillus thuringiensis (Bt) kurstaki @ 2g/L or Metarhizium anisopliae @ 5g/L.",
                organicCureHi = "पौधे की गोभ में सूखी राख या बालू में नीम की खली का चूर्ण (9:1) मिलाकर एक चुटकी डालें। बीटी (Bt 2 ग्राम/लीटर) का छिड़काव करें।",
                organicCureBn = "গাছের মাঝখানে ছাই বা বালি ও নিমের গুঁড়োর মিশ্রণ এক চিমটি করে দিন এবং বিটি বা মেটারহাইজিয়াম স্প্রে করুন।",
                chemicalCureEn = "Spray Emamectin Benzoate 5% SG @ 0.4 g/L or Chlorantraniliprole 18.5% SC (Coragen @ 0.4 ml/L) directed directly into the central whorl funnel.",
                chemicalCureHi = "पौधे की गोभ (पोंगो) को निशाना बनाकर इमामेक्टिन बेंजोएट 5% एसजी (0.4 ग्राम/लीटर) या कोराजन (0.4 मिली/लीटर) का छिड़काव करें।",
                chemicalCureBn = "গাছের কেন্দ্রে সরাসরি ইমাজেক্টিন বেনজয়েট ০.৪ গ্রাম প্রতি লিটার অথবা কোরাজন ০.৪ মিলি প্রতি লিটার জলে স্প্রে করুন।",
                preventionEn = "Erect pheromone traps @ 5/acre for moth monitoring. Intercrop with desmodium or cowpea (push-pull strategy).",
                preventionHi = "नर पतंगों की निगरानी हेतु 5 फेरोमोन ट्रैप प्रति एकड़ लगाएं। मक्के के साथ लोबिया (कौपी) की अंतर्वर्ती खेती करें।",
                preventionBn = "একর প্রতি ৫টি ফেরোমন ফাঁদ বসান এবং ভুট্টার সাথে বরবটি বা ডালজাতীয় শস্য চাষ করুন।"
            )
        )
    )

    fun findMatchingDisease(cropHint: String, problemName: String): IndianCropDisease? {
        val query = "$cropHint $problemName".lowercase()

        // 1. Direct match by crop and disease key words
        return allDiseases.firstOrNull { disease ->
            val matchesCrop = query.contains(disease.cropNameEn.lowercase().take(5)) ||
                    query.contains(disease.cropNameHi.lowercase()) ||
                    query.contains(disease.cropNameBn.lowercase())

            val matchesDisease = query.contains(disease.diseaseNameEn.lowercase().take(5)) ||
                    query.contains(disease.scientificName.lowercase().take(6)) ||
                    disease.diseaseNameEn.lowercase().split(" ").any { word -> word.length > 3 && query.contains(word) }

            matchesCrop && matchesDisease
        } ?: allDiseases.firstOrNull { disease ->
            // Fallback: match by crop
            query.contains(disease.cropNameEn.lowercase().take(5)) ||
                    query.contains(disease.cropNameHi.lowercase())
        }
    }

    fun getDiseasesForCrop(cropName: String): List<IndianCropDisease> {
        val cleanCrop = cropName.lowercase()
        return allDiseases.filter {
            cleanCrop.contains(it.cropNameEn.lowercase().take(4)) ||
                    it.cropNameEn.lowercase().contains(cleanCrop.take(4))
        }
    }
}
