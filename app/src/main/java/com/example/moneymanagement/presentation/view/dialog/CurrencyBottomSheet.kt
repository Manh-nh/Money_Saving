package com.example.moneymanagement.presentation.view.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.databinding.BottomSheetSelectCurrencyBinding
import com.example.moneymanagement.presentation.database.model.CurrencyItem
import com.example.moneymanagement.presentation.view.adapter.CurrencyAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CurrencyBottomSheet(
    private val selectedCode: String,
    private val onCurrencySelected: (CurrencyItem) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSelectCurrencyBinding? = null
    private val binding get() = _binding!!

    private val allCurrencies = listOf(
        CurrencyItem("USD", "United States Dollar"),
        CurrencyItem("VND", "Vietnamese Đồng"),
        CurrencyItem("EUR", "Euro"),
        CurrencyItem("GBP", "British Pound Sterling"),
        CurrencyItem("JPY", "Japanese Yen"),
        CurrencyItem("CAD", "Canadian Dollar"),
        CurrencyItem("AUD", "Australian Dollar"),
        CurrencyItem("CHF", "Swiss Franc"),
        CurrencyItem("CNY", "Chinese Yuan"),
        CurrencyItem("SEK", "Swedish Krona"),
        CurrencyItem("NZD", "New Zealand Dollar"),
        CurrencyItem("KRW", "South Korean Won"),
        CurrencyItem("SGD", "Singapore Dollar"),
        CurrencyItem("INR", "Indian Rupee"),
        CurrencyItem("RUB", "Russian Ruble"),
        CurrencyItem("ZAR", "South African Rand"),
        CurrencyItem("TRY", "Turkish Lira"),
        CurrencyItem("BRL", "Brazilian Real"),
        CurrencyItem("TWD", "Taiwan New Dollar"),
        CurrencyItem("DKK", "Danish Krone"),
        CurrencyItem("PLN", "Polish Zloty"),
        CurrencyItem("THB", "Thai Baht"),
        CurrencyItem("IDR", "Indonesian Rupiah"),
        CurrencyItem("HUF", "Hungarian Forint"),
        CurrencyItem("CZK", "Czech Koruna"),
        CurrencyItem("ILS", "Israeli New Sheqel"),
        CurrencyItem("CLP", "Chilean Peso"),
        CurrencyItem("PHP", "Philippine Peso"),
        CurrencyItem("AED", "UAE Dirham"),
        CurrencyItem("COP", "Colombian Peso"),
        CurrencyItem("SAR", "Saudi Riyal"),
        CurrencyItem("MYR", "Malaysian Ringgit"),
        CurrencyItem("RON", "Romanian Leu"),
        CurrencyItem("AFN", "Afghan Afghani"),
        CurrencyItem("ALL", "Albanian Lek"),
        CurrencyItem("AMD", "Armenian Dram"),
        CurrencyItem("ANG", "Netherlands Antillean Guilder"),
        CurrencyItem("AOA", "Angolan Kwanza"),
        CurrencyItem("ARS", "Argentine Peso"),
        CurrencyItem("AWG", "Aruban Florin"),
        CurrencyItem("AZN", "Azerbaijani Manat"),
        CurrencyItem("BAM", "Bosnia-Herzegovina Convertible Mark"),
        CurrencyItem("BBD", "Barbadian Dollar"),
        CurrencyItem("BDT", "Bangladeshi Taka"),
        CurrencyItem("BGN", "Bulgarian Lev"),
        CurrencyItem("BHD", "Bahraini Dinar"),
        CurrencyItem("BIF", "Burundian Franc"),
        CurrencyItem("BMD", "Bermudan Dollar"),
        CurrencyItem("BND", "Brunei Dollar"),
        CurrencyItem("BOB", "Bolivian Boliviano"),
        CurrencyItem("BSD", "Bahamian Dollar"),
        CurrencyItem("BTN", "Bhutanese Ngultrum"),
        CurrencyItem("BWP", "Botswanan Pula"),
        CurrencyItem("BYN", "Belarusian Ruble"),
        CurrencyItem("BZD", "Belize Dollar"),
        CurrencyItem("CDF", "Congolese Franc"),
        CurrencyItem("CRC", "Costa Rican Colón"),
        CurrencyItem("CUP", "Cuban Peso"),
        CurrencyItem("CVE", "Cape Verdean Escudo"),
        CurrencyItem("DJF", "Djiboutian Franc"),
        CurrencyItem("DOP", "Dominican Peso"),
        CurrencyItem("DZD", "Algerian Dinar"),
        CurrencyItem("EGP", "Egyptian Pound"),
        CurrencyItem("ERN", "Eritrean Nakfa"),
        CurrencyItem("ETB", "Ethiopian Birr"),
        CurrencyItem("FJD", "Fijian Dollar"),
        CurrencyItem("FKP", "Falkland Islands Pound"),
        CurrencyItem("FOK", "Faroese Króna"),
        CurrencyItem("GEL", "Georgian Lari"),
        CurrencyItem("GGP", "Guernsey Pound"),
        CurrencyItem("GHS", "Ghanaian Cedi"),
        CurrencyItem("GIP", "Gibraltar Pound"),
        CurrencyItem("GMD", "Gambian Dalasi"),
        CurrencyItem("GNF", "Guinean Franc"),
        CurrencyItem("GTQ", "Guatemalan Quetzal"),
        CurrencyItem("GYD", "Guyanese Dollar"),
        CurrencyItem("HKD", "Hong Kong Dollar"),
        CurrencyItem("HNL", "Honduran Lempira"),
        CurrencyItem("HRK", "Croatian Kuna"),
        CurrencyItem("HTG", "Haitian Gourde"),
        CurrencyItem("IQD", "Iraqi Dinar"),
        CurrencyItem("IRR", "Iranian Rial"),
        CurrencyItem("ISK", "Icelandic Króna"),
        CurrencyItem("JEP", "Jersey Pound"),
        CurrencyItem("JMD", "Jamaican Dollar"),
        CurrencyItem("JOD", "Jordanian Dinar"),
        CurrencyItem("KES", "Kenyan Shilling"),
        CurrencyItem("KGS", "Kyrgystani Som"),
        CurrencyItem("KHR", "Cambodian Riel"),
        CurrencyItem("KID", "Kiribati Dollar"),
        CurrencyItem("KMF", "Comorian Franc"),
        CurrencyItem("KWD", "Kuwaiti Dinar"),
        CurrencyItem("KYD", "Cayman Islands Dollar"),
        CurrencyItem("KZT", "Kazakhstani Tenge"),
        CurrencyItem("LAK", "Laotian Kip"),
        CurrencyItem("LBP", "Lebanese Pound"),
        CurrencyItem("LKR", "Sri Lankan Rupee"),
        CurrencyItem("LRD", "Liberian Dollar"),
        CurrencyItem("LSL", "Lesotho Loti"),
        CurrencyItem("LYD", "Libyan Dinar"),
        CurrencyItem("MAD", "Moroccan Dirham"),
        CurrencyItem("MDL", "Moldovan Leu"),
        CurrencyItem("MGA", "Malagasy Ariary"),
        CurrencyItem("MKD", "Macedonian Denar"),
        CurrencyItem("MMK", "Myanmar Kyat"),
        CurrencyItem("MNT", "Mongolian Tugrik"),
        CurrencyItem("MOP", "Macanese Pataca"),
        CurrencyItem("MRU", "Mauritanian Ouguiya"),
        CurrencyItem("MUR", "Mauritian Rupee"),
        CurrencyItem("MVR", "Maldivian Rufiyaa"),
        CurrencyItem("MWK", "Malawian Kwacha"),
        CurrencyItem("MZN", "Mozambican Metical"),
        CurrencyItem("NAD", "Namibian Dollar"),
        CurrencyItem("NGN", "Nigerian Naira"),
        CurrencyItem("NIO", "Nicaraguan Córdoba"),
        CurrencyItem("NPR", "Nepalese Rupee"),
        CurrencyItem("OMR", "Omani Rial"),
        CurrencyItem("PAB", "Panamanian Balboa"),
        CurrencyItem("PEN", "Peruvian Sol"),
        CurrencyItem("PGK", "Papua New Guinean Kina"),
        CurrencyItem("PKR", "Pakistani Rupee"),
        CurrencyItem("PYG", "Paraguayan Guarani"),
        CurrencyItem("QAR", "Qatari Rial"),
        CurrencyItem("RSD", "Serbian Dinar"),
        CurrencyItem("RWF", "Rwandan Franc"),
        CurrencyItem("SBD", "Solomon Islands Dollar"),
        CurrencyItem("SCR", "Seychellois Rupee"),
        CurrencyItem("SDG", "Sudanese Pound"),
        CurrencyItem("SHP", "Saint Helena Pound"),
        CurrencyItem("SLL", "Sierra Leonean Leone"),
        CurrencyItem("SOS", "Somali Shilling"),
        CurrencyItem("SRD", "Surinamese Dollar"),
        CurrencyItem("SSP", "South Sudanese Pound"),
        CurrencyItem("STN", "São Tomé and Príncipe Dobra"),
        CurrencyItem("SYP", "Syrian Pound"),
        CurrencyItem("SZL", "Eswatini Lilangeni"),
        CurrencyItem("TJS", "Tajikistani Somoni"),
        CurrencyItem("TMT", "Turkmenistani Manat"),
        CurrencyItem("TND", "Tunisian Dinar"),
        CurrencyItem("TOP", "Tongan Paʻanga"),
        CurrencyItem("TTD", "Trinidad and Tobago Dollar"),
        CurrencyItem("TVD", "Tuvaluan Dollar"),
        CurrencyItem("TZS", "Tanzanian Shilling"),
        CurrencyItem("UAH", "Ukrainian Hryvnia"),
        CurrencyItem("UGX", "Ugandan Shilling"),
        CurrencyItem("UYU", "Uruguayan Peso"),
        CurrencyItem("UZS", "Uzbekistani Som"),
        CurrencyItem("VES", "Venezuelan Bolívar"),
        CurrencyItem("VUV", "Vanuatu Vatu"),
        CurrencyItem("WST", "Samoan Tala"),
        CurrencyItem("XAF", "Central African CFA Franc"),
        CurrencyItem("XCD", "East Caribbean Dollar"),
        CurrencyItem("XDR", "Special Drawing Rights"),
        CurrencyItem("XOF", "West African CFA Franc"),
        CurrencyItem("XPF", "CFP Franc"),
        CurrencyItem("YER", "Yemeni Rial"),
        CurrencyItem("ZMW", "Zambian Kwacha")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSelectCurrencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CurrencyAdapter(allCurrencies.sortedBy { it.code }, selectedCode) { currency ->
            onCurrencySelected(currency)
            dismiss()
        }

        binding.rvCurrencies.layoutManager = LinearLayoutManager(context)
        binding.rvCurrencies.adapter = adapter

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
