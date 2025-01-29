package com.example.agrease

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class Solution : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_solution, container, false)
        // get the label from bundle
        val label = arguments?.getString("label")

        // get the solution from the label from the string resources
        val solutions = mapOf(
            "normal" to R.array.normal,
            "hawar_daun_padi" to R.array.hawar_daun_padi,
            "gores_daun_padi" to R.array.gores_daun_padi,
            "hawar_malai_padi" to R.array.hawar_malai_padi,
            "blas" to R.array.blas,
            "bercak_coklat_pada_padi" to R.array.bercak_coklat_pada_padi,
            "malai_mati" to R.array.malai_mati,
            "bulai_padi" to R.array.bulai_padi,
            "hispa" to R.array.hispa,
            "tungro" to R.array.tungro,
            "Bercak Bakteri" to R.array.bercak_bakteri,
            "bercak_daun" to R.array.bercak_daun,
            "bercak_daun_septoria" to R.array.bercak_daun_septoria,
            "bercak_kering" to R.array.bercak_kering,
            "bintik_coklat_daun" to R.array.bintik_coklat_daun,
            "busuk_daun" to R.array.busuk_daun,
            "daun_keriting_kuning" to R.array.daun_keriting_kuning,
            "gangguan_tungau_merah_dan_tungau_laba_Laba" to R.array.gangguan_tungau_merah_dan_tungau_laba_laba,
            "penyakit_keriting" to R.array.penyakit_keriting,
            "bercak_busuk_daun_kentang" to R.array.bercak_busuk_daun_kentang,
            "bercak_kering_daun_kentang" to R.array.bercak_kering_daun_ketang,
            "bercak_daun_abu_abu" to R.array.bercak_daun_abu_abu,
            "daun_sehat" to R.array.daun_sehat,
            "hawar_daun" to R.array.hawar_daun_jagung,
            "Karat Daun" to R.array.karat_daun_jagung
            )

        // replace the label with the solution
        val solution = solutions[label]
        val penyebab = solution?.let { resources.getStringArray(it)[0] }
        val pencegahan = solution?.let { resources.getStringArray(it)[1] }
        val pengobatan = solution?.let { resources.getStringArray(it)[2] }

        // set the solution to the text view
        view.findViewById<TextView>(R.id.penyebab).text = penyebab
        view.findViewById<TextView>(R.id.pencegahan).text = pencegahan
        view.findViewById<TextView>(R.id.pengobatan).text = pengobatan

        // Inflate the layout for this fragment
        return view
    }

}