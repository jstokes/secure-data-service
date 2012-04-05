//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.30 at 01:48:06 PM EDT 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LanguageItemType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LanguageItemType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Spanish"/>
 *     &lt;enumeration value="Vietnamese"/>
 *     &lt;enumeration value="Laotian (Lao)"/>
 *     &lt;enumeration value="Cambodian (Khmer)"/>
 *     &lt;enumeration value="Korean"/>
 *     &lt;enumeration value="Japanese"/>
 *     &lt;enumeration value="French"/>
 *     &lt;enumeration value="German"/>
 *     &lt;enumeration value="English"/>
 *     &lt;enumeration value="Other languages"/>
 *     &lt;enumeration value="Afrikaans (Taal)"/>
 *     &lt;enumeration value="Akan (Fante, Asante)"/>
 *     &lt;enumeration value="Albanian, Gheg (Kossovo/Macedonia)"/>
 *     &lt;enumeration value="Albanian, Tosk (Albania)"/>
 *     &lt;enumeration value="Algonquin"/>
 *     &lt;enumeration value="Amharic"/>
 *     &lt;enumeration value="Apache"/>
 *     &lt;enumeration value="Arabic"/>
 *     &lt;enumeration value="Armenian"/>
 *     &lt;enumeration value="Assyrian (Syriac, Aramaic)"/>
 *     &lt;enumeration value="Balinese"/>
 *     &lt;enumeration value="Bengali"/>
 *     &lt;enumeration value="Bosnian"/>
 *     &lt;enumeration value="Bulgarian"/>
 *     &lt;enumeration value="Burmese"/>
 *     &lt;enumeration value="Cantonese (Chinese)"/>
 *     &lt;enumeration value="Cebuano (Visayan)"/>
 *     &lt;enumeration value="Chamorro"/>
 *     &lt;enumeration value="Chaochow/Teochiu (Chinese)"/>
 *     &lt;enumeration value="Cherokee"/>
 *     &lt;enumeration value="Chippewa/Ojibawa/Ottawa"/>
 *     &lt;enumeration value="Choctaw"/>
 *     &lt;enumeration value="Comanche"/>
 *     &lt;enumeration value="Coushatta"/>
 *     &lt;enumeration value="Creek"/>
 *     &lt;enumeration value="Croatian"/>
 *     &lt;enumeration value="Crow"/>
 *     &lt;enumeration value="Czech"/>
 *     &lt;enumeration value="Danish"/>
 *     &lt;enumeration value="Dard"/>
 *     &lt;enumeration value="Dutch/Flemish"/>
 *     &lt;enumeration value="Efik"/>
 *     &lt;enumeration value="Eskimo"/>
 *     &lt;enumeration value="Estonian"/>
 *     &lt;enumeration value="Ethiopic"/>
 *     &lt;enumeration value="Ewe"/>
 *     &lt;enumeration value="Farsi (Persian)"/>
 *     &lt;enumeration value="Finnish"/>
 *     &lt;enumeration value="Fukien/Hokkien (Chinese)"/>
 *     &lt;enumeration value="Gaelic (Irish)"/>
 *     &lt;enumeration value="Gaelic (Scottish)"/>
 *     &lt;enumeration value="Greek"/>
 *     &lt;enumeration value="Gujarati"/>
 *     &lt;enumeration value="Guyanese"/>
 *     &lt;enumeration value="Hainanese (Chinese)"/>
 *     &lt;enumeration value="Haitian-Creole"/>
 *     &lt;enumeration value="Hakka (Chinese)"/>
 *     &lt;enumeration value="Hausa"/>
 *     &lt;enumeration value="Hebrew"/>
 *     &lt;enumeration value="Hindi"/>
 *     &lt;enumeration value="Hmong"/>
 *     &lt;enumeration value="Hopi"/>
 *     &lt;enumeration value="Hungarian"/>
 *     &lt;enumeration value="Ibo/Igbo"/>
 *     &lt;enumeration value="Icelandic"/>
 *     &lt;enumeration value="Ilonggo (Hiligaynon)"/>
 *     &lt;enumeration value="Indonesian"/>
 *     &lt;enumeration value="Italian"/>
 *     &lt;enumeration value="Kache (Kaje, Jju)"/>
 *     &lt;enumeration value="Kannada (Kanarese)"/>
 *     &lt;enumeration value="Kanuri"/>
 *     &lt;enumeration value="Kashmiri"/>
 *     &lt;enumeration value="Kickapoo"/>
 *     &lt;enumeration value="Konkani"/>
 *     &lt;enumeration value="Kpelle"/>
 *     &lt;enumeration value="Krio"/>
 *     &lt;enumeration value="Kurdish"/>
 *     &lt;enumeration value="Kwa"/>
 *     &lt;enumeration value="Latvian"/>
 *     &lt;enumeration value="Lingala"/>
 *     &lt;enumeration value="Lithuanian"/>
 *     &lt;enumeration value="Luganda"/>
 *     &lt;enumeration value="Lunda"/>
 *     &lt;enumeration value="Luyia (Luhya)"/>
 *     &lt;enumeration value="Macedonian"/>
 *     &lt;enumeration value="Malay"/>
 *     &lt;enumeration value="Malayalam"/>
 *     &lt;enumeration value="Maltese"/>
 *     &lt;enumeration value="Mandarin (Chinese)"/>
 *     &lt;enumeration value="Mande"/>
 *     &lt;enumeration value="Marathi"/>
 *     &lt;enumeration value="Menominee"/>
 *     &lt;enumeration value="Mien (Yao)"/>
 *     &lt;enumeration value="Navajo"/>
 *     &lt;enumeration value="Nepali"/>
 *     &lt;enumeration value="Norwegian"/>
 *     &lt;enumeration value="Okinawan"/>
 *     &lt;enumeration value="Oneida"/>
 *     &lt;enumeration value="Oriya"/>
 *     &lt;enumeration value="Orri (Oring)"/>
 *     &lt;enumeration value="Pampangan"/>
 *     &lt;enumeration value="Panjabi (Punjabi)"/>
 *     &lt;enumeration value="Pashto (Pushto)"/>
 *     &lt;enumeration value="Pilipino (Tagalog)"/>
 *     &lt;enumeration value="Pima"/>
 *     &lt;enumeration value="Polish"/>
 *     &lt;enumeration value="Portuguese"/>
 *     &lt;enumeration value="Pueblo"/>
 *     &lt;enumeration value="Romanian"/>
 *     &lt;enumeration value="Romany (Gypsy)"/>
 *     &lt;enumeration value="Russian"/>
 *     &lt;enumeration value="Samoan"/>
 *     &lt;enumeration value="Serbian"/>
 *     &lt;enumeration value="Shanghai (Chinese)"/>
 *     &lt;enumeration value="Shona"/>
 *     &lt;enumeration value="Sikkimese"/>
 *     &lt;enumeration value="Sindhi"/>
 *     &lt;enumeration value="Sinhalese (Sri Lanka)"/>
 *     &lt;enumeration value="Sioux (Dakota)"/>
 *     &lt;enumeration value="Slavic"/>
 *     &lt;enumeration value="Slovenian (Slovene)"/>
 *     &lt;enumeration value="Somali"/>
 *     &lt;enumeration value="Sotho"/>
 *     &lt;enumeration value="Swahili"/>
 *     &lt;enumeration value="Swedish"/>
 *     &lt;enumeration value="Taiwanese/Formosan/Min Nan (Chinese)"/>
 *     &lt;enumeration value="Tamil"/>
 *     &lt;enumeration value="Telugu (Telegu)"/>
 *     &lt;enumeration value="Thai"/>
 *     &lt;enumeration value="Tibetan"/>
 *     &lt;enumeration value="Tigrinya"/>
 *     &lt;enumeration value="Tiwa"/>
 *     &lt;enumeration value="Tuluau"/>
 *     &lt;enumeration value="Turkish"/>
 *     &lt;enumeration value="Ukrainian"/>
 *     &lt;enumeration value="Urdu"/>
 *     &lt;enumeration value="Welsh"/>
 *     &lt;enumeration value="Winnebago"/>
 *     &lt;enumeration value="Yiddish"/>
 *     &lt;enumeration value="Yombe"/>
 *     &lt;enumeration value="Yoruba"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LanguageItemType")
@XmlEnum
public enum LanguageItemType {

    @XmlEnumValue("Spanish")
    SPANISH("Spanish"),
    @XmlEnumValue("Vietnamese")
    VIETNAMESE("Vietnamese"),
    @XmlEnumValue("Laotian (Lao)")
    LAOTIAN_LAO("Laotian (Lao)"),
    @XmlEnumValue("Cambodian (Khmer)")
    CAMBODIAN_KHMER("Cambodian (Khmer)"),
    @XmlEnumValue("Korean")
    KOREAN("Korean"),
    @XmlEnumValue("Japanese")
    JAPANESE("Japanese"),
    @XmlEnumValue("French")
    FRENCH("French"),
    @XmlEnumValue("German")
    GERMAN("German"),
    @XmlEnumValue("English")
    ENGLISH("English"),
    @XmlEnumValue("Other languages")
    OTHER_LANGUAGES("Other languages"),
    @XmlEnumValue("Afrikaans (Taal)")
    AFRIKAANS_TAAL("Afrikaans (Taal)"),
    @XmlEnumValue("Akan (Fante, Asante)")
    AKAN_FANTE_ASANTE("Akan (Fante, Asante)"),
    @XmlEnumValue("Albanian, Gheg (Kossovo/Macedonia)")
    ALBANIAN_GHEG_KOSSOVO_MACEDONIA("Albanian, Gheg (Kossovo/Macedonia)"),
    @XmlEnumValue("Albanian, Tosk (Albania)")
    ALBANIAN_TOSK_ALBANIA("Albanian, Tosk (Albania)"),
    @XmlEnumValue("Algonquin")
    ALGONQUIN("Algonquin"),
    @XmlEnumValue("Amharic")
    AMHARIC("Amharic"),
    @XmlEnumValue("Apache")
    APACHE("Apache"),
    @XmlEnumValue("Arabic")
    ARABIC("Arabic"),
    @XmlEnumValue("Armenian")
    ARMENIAN("Armenian"),
    @XmlEnumValue("Assyrian (Syriac, Aramaic)")
    ASSYRIAN_SYRIAC_ARAMAIC("Assyrian (Syriac, Aramaic)"),
    @XmlEnumValue("Balinese")
    BALINESE("Balinese"),
    @XmlEnumValue("Bengali")
    BENGALI("Bengali"),
    @XmlEnumValue("Bosnian")
    BOSNIAN("Bosnian"),
    @XmlEnumValue("Bulgarian")
    BULGARIAN("Bulgarian"),
    @XmlEnumValue("Burmese")
    BURMESE("Burmese"),
    @XmlEnumValue("Cantonese (Chinese)")
    CANTONESE_CHINESE("Cantonese (Chinese)"),
    @XmlEnumValue("Cebuano (Visayan)")
    CEBUANO_VISAYAN("Cebuano (Visayan)"),
    @XmlEnumValue("Chamorro")
    CHAMORRO("Chamorro"),
    @XmlEnumValue("Chaochow/Teochiu (Chinese)")
    CHAOCHOW_TEOCHIU_CHINESE("Chaochow/Teochiu (Chinese)"),
    @XmlEnumValue("Cherokee")
    CHEROKEE("Cherokee"),
    @XmlEnumValue("Chippewa/Ojibawa/Ottawa")
    CHIPPEWA_OJIBAWA_OTTAWA("Chippewa/Ojibawa/Ottawa"),
    @XmlEnumValue("Choctaw")
    CHOCTAW("Choctaw"),
    @XmlEnumValue("Comanche")
    COMANCHE("Comanche"),
    @XmlEnumValue("Coushatta")
    COUSHATTA("Coushatta"),
    @XmlEnumValue("Creek")
    CREEK("Creek"),
    @XmlEnumValue("Croatian")
    CROATIAN("Croatian"),
    @XmlEnumValue("Crow")
    CROW("Crow"),
    @XmlEnumValue("Czech")
    CZECH("Czech"),
    @XmlEnumValue("Danish")
    DANISH("Danish"),
    @XmlEnumValue("Dard")
    DARD("Dard"),
    @XmlEnumValue("Dutch/Flemish")
    DUTCH_FLEMISH("Dutch/Flemish"),
    @XmlEnumValue("Efik")
    EFIK("Efik"),
    @XmlEnumValue("Eskimo")
    ESKIMO("Eskimo"),
    @XmlEnumValue("Estonian")
    ESTONIAN("Estonian"),
    @XmlEnumValue("Ethiopic")
    ETHIOPIC("Ethiopic"),
    @XmlEnumValue("Ewe")
    EWE("Ewe"),
    @XmlEnumValue("Farsi (Persian)")
    FARSI_PERSIAN("Farsi (Persian)"),
    @XmlEnumValue("Finnish")
    FINNISH("Finnish"),
    @XmlEnumValue("Fukien/Hokkien (Chinese)")
    FUKIEN_HOKKIEN_CHINESE("Fukien/Hokkien (Chinese)"),
    @XmlEnumValue("Gaelic (Irish)")
    GAELIC_IRISH("Gaelic (Irish)"),
    @XmlEnumValue("Gaelic (Scottish)")
    GAELIC_SCOTTISH("Gaelic (Scottish)"),
    @XmlEnumValue("Greek")
    GREEK("Greek"),
    @XmlEnumValue("Gujarati")
    GUJARATI("Gujarati"),
    @XmlEnumValue("Guyanese")
    GUYANESE("Guyanese"),
    @XmlEnumValue("Hainanese (Chinese)")
    HAINANESE_CHINESE("Hainanese (Chinese)"),
    @XmlEnumValue("Haitian-Creole")
    HAITIAN_CREOLE("Haitian-Creole"),
    @XmlEnumValue("Hakka (Chinese)")
    HAKKA_CHINESE("Hakka (Chinese)"),
    @XmlEnumValue("Hausa")
    HAUSA("Hausa"),
    @XmlEnumValue("Hebrew")
    HEBREW("Hebrew"),
    @XmlEnumValue("Hindi")
    HINDI("Hindi"),
    @XmlEnumValue("Hmong")
    HMONG("Hmong"),
    @XmlEnumValue("Hopi")
    HOPI("Hopi"),
    @XmlEnumValue("Hungarian")
    HUNGARIAN("Hungarian"),
    @XmlEnumValue("Ibo/Igbo")
    IBO_IGBO("Ibo/Igbo"),
    @XmlEnumValue("Icelandic")
    ICELANDIC("Icelandic"),
    @XmlEnumValue("Ilonggo (Hiligaynon)")
    ILONGGO_HILIGAYNON("Ilonggo (Hiligaynon)"),
    @XmlEnumValue("Indonesian")
    INDONESIAN("Indonesian"),
    @XmlEnumValue("Italian")
    ITALIAN("Italian"),
    @XmlEnumValue("Kache (Kaje, Jju)")
    KACHE_KAJE_JJU("Kache (Kaje, Jju)"),
    @XmlEnumValue("Kannada (Kanarese)")
    KANNADA_KANARESE("Kannada (Kanarese)"),
    @XmlEnumValue("Kanuri")
    KANURI("Kanuri"),
    @XmlEnumValue("Kashmiri")
    KASHMIRI("Kashmiri"),
    @XmlEnumValue("Kickapoo")
    KICKAPOO("Kickapoo"),
    @XmlEnumValue("Konkani")
    KONKANI("Konkani"),
    @XmlEnumValue("Kpelle")
    KPELLE("Kpelle"),
    @XmlEnumValue("Krio")
    KRIO("Krio"),
    @XmlEnumValue("Kurdish")
    KURDISH("Kurdish"),
    @XmlEnumValue("Kwa")
    KWA("Kwa"),
    @XmlEnumValue("Latvian")
    LATVIAN("Latvian"),
    @XmlEnumValue("Lingala")
    LINGALA("Lingala"),
    @XmlEnumValue("Lithuanian")
    LITHUANIAN("Lithuanian"),
    @XmlEnumValue("Luganda")
    LUGANDA("Luganda"),
    @XmlEnumValue("Lunda")
    LUNDA("Lunda"),
    @XmlEnumValue("Luyia (Luhya)")
    LUYIA_LUHYA("Luyia (Luhya)"),
    @XmlEnumValue("Macedonian")
    MACEDONIAN("Macedonian"),
    @XmlEnumValue("Malay")
    MALAY("Malay"),
    @XmlEnumValue("Malayalam")
    MALAYALAM("Malayalam"),
    @XmlEnumValue("Maltese")
    MALTESE("Maltese"),
    @XmlEnumValue("Mandarin (Chinese)")
    MANDARIN_CHINESE("Mandarin (Chinese)"),
    @XmlEnumValue("Mande")
    MANDE("Mande"),
    @XmlEnumValue("Marathi")
    MARATHI("Marathi"),
    @XmlEnumValue("Menominee")
    MENOMINEE("Menominee"),
    @XmlEnumValue("Mien (Yao)")
    MIEN_YAO("Mien (Yao)"),
    @XmlEnumValue("Navajo")
    NAVAJO("Navajo"),
    @XmlEnumValue("Nepali")
    NEPALI("Nepali"),
    @XmlEnumValue("Norwegian")
    NORWEGIAN("Norwegian"),
    @XmlEnumValue("Okinawan")
    OKINAWAN("Okinawan"),
    @XmlEnumValue("Oneida")
    ONEIDA("Oneida"),
    @XmlEnumValue("Oriya")
    ORIYA("Oriya"),
    @XmlEnumValue("Orri (Oring)")
    ORRI_ORING("Orri (Oring)"),
    @XmlEnumValue("Pampangan")
    PAMPANGAN("Pampangan"),
    @XmlEnumValue("Panjabi (Punjabi)")
    PANJABI_PUNJABI("Panjabi (Punjabi)"),
    @XmlEnumValue("Pashto (Pushto)")
    PASHTO_PUSHTO("Pashto (Pushto)"),
    @XmlEnumValue("Pilipino (Tagalog)")
    PILIPINO_TAGALOG("Pilipino (Tagalog)"),
    @XmlEnumValue("Pima")
    PIMA("Pima"),
    @XmlEnumValue("Polish")
    POLISH("Polish"),
    @XmlEnumValue("Portuguese")
    PORTUGUESE("Portuguese"),
    @XmlEnumValue("Pueblo")
    PUEBLO("Pueblo"),
    @XmlEnumValue("Romanian")
    ROMANIAN("Romanian"),
    @XmlEnumValue("Romany (Gypsy)")
    ROMANY_GYPSY("Romany (Gypsy)"),
    @XmlEnumValue("Russian")
    RUSSIAN("Russian"),
    @XmlEnumValue("Samoan")
    SAMOAN("Samoan"),
    @XmlEnumValue("Serbian")
    SERBIAN("Serbian"),
    @XmlEnumValue("Shanghai (Chinese)")
    SHANGHAI_CHINESE("Shanghai (Chinese)"),
    @XmlEnumValue("Shona")
    SHONA("Shona"),
    @XmlEnumValue("Sikkimese")
    SIKKIMESE("Sikkimese"),
    @XmlEnumValue("Sindhi")
    SINDHI("Sindhi"),
    @XmlEnumValue("Sinhalese (Sri Lanka)")
    SINHALESE_SRI_LANKA("Sinhalese (Sri Lanka)"),
    @XmlEnumValue("Sioux (Dakota)")
    SIOUX_DAKOTA("Sioux (Dakota)"),
    @XmlEnumValue("Slavic")
    SLAVIC("Slavic"),
    @XmlEnumValue("Slovenian (Slovene)")
    SLOVENIAN_SLOVENE("Slovenian (Slovene)"),
    @XmlEnumValue("Somali")
    SOMALI("Somali"),
    @XmlEnumValue("Sotho")
    SOTHO("Sotho"),
    @XmlEnumValue("Swahili")
    SWAHILI("Swahili"),
    @XmlEnumValue("Swedish")
    SWEDISH("Swedish"),
    @XmlEnumValue("Taiwanese/Formosan/Min Nan (Chinese)")
    TAIWANESE_FORMOSAN_MIN_NAN_CHINESE("Taiwanese/Formosan/Min Nan (Chinese)"),
    @XmlEnumValue("Tamil")
    TAMIL("Tamil"),
    @XmlEnumValue("Telugu (Telegu)")
    TELUGU_TELEGU("Telugu (Telegu)"),
    @XmlEnumValue("Thai")
    THAI("Thai"),
    @XmlEnumValue("Tibetan")
    TIBETAN("Tibetan"),
    @XmlEnumValue("Tigrinya")
    TIGRINYA("Tigrinya"),
    @XmlEnumValue("Tiwa")
    TIWA("Tiwa"),
    @XmlEnumValue("Tuluau")
    TULUAU("Tuluau"),
    @XmlEnumValue("Turkish")
    TURKISH("Turkish"),
    @XmlEnumValue("Ukrainian")
    UKRAINIAN("Ukrainian"),
    @XmlEnumValue("Urdu")
    URDU("Urdu"),
    @XmlEnumValue("Welsh")
    WELSH("Welsh"),
    @XmlEnumValue("Winnebago")
    WINNEBAGO("Winnebago"),
    @XmlEnumValue("Yiddish")
    YIDDISH("Yiddish"),
    @XmlEnumValue("Yombe")
    YOMBE("Yombe"),
    @XmlEnumValue("Yoruba")
    YORUBA("Yoruba");
    private final String value;

    LanguageItemType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LanguageItemType fromValue(String v) {
        for (LanguageItemType c: LanguageItemType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
