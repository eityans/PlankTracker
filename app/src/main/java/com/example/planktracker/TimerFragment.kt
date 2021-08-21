package com.example.planktracker

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.planktracker.databinding.FragmentTimerBinding
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.util.*


class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setFabVisible(View.INVISIBLE)
        binding.save.setOnClickListener { savePlank(it) }
    }

    private fun savePlank(view: View) {
        realm.executeTransaction { db: Realm ->
            val maxId = db.where<Plank>().max("id")
            val nextId = (maxId?.toLong() ?: 0L) + 1L
            val plank = db.createObject<Plank>(nextId)
            val date = Date(System.currentTimeMillis())
            plank.date = date
            plank.sec = Integer.parseInt(binding.secEdit.text.toString())
        }
        Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)
            .setAction("戻る") { findNavController().popBackStack() }
            .setActionTextColor(Color.YELLOW)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

//    private fun getNowDate(): String {
//        val df: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
//        val date = Date(System.currentTimeMillis())
//        return df.format(date)
//    }


}