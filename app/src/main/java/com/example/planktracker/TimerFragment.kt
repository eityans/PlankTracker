package com.example.planktracker

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import java.text.SimpleDateFormat
import java.util.*


class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm

    private var count: Long = 0L
    private var period: Long = 1000L   // 1000ミリ秒で更新する
    private var isRunning = false
    private val dataFormat: SimpleDateFormat = SimpleDateFormat("mm:ss", Locale.US)

    // 'Handler()' is deprecated as of API 30: Android 11.0 (R)
    private val handler: Handler = Handler(Looper.getMainLooper())

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            count++
            binding.timerText.text = dataFormat.format(count * period)
            handler.postDelayed(this, period)
        }
    }

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

        binding.timerText.text = dataFormat.format(0)

        binding.saveActionButton.setOnClickListener { savePlank(it) }
        binding.playStop.setOnClickListener {
            when (isRunning) {
                true -> {
                    handler.removeCallbacks(runnable);
                }
                false -> {
                    handler.post(runnable);
                }
            }
            isRunning = !isRunning
        }

    }

    private fun savePlank(view: View) {
        realm.executeTransaction { db: Realm ->
            val maxId = db.where<Plank>().max("id")
            val nextId = (maxId?.toLong() ?: 0L) + 1L
            val plank = db.createObject<Plank>(nextId)
            val date = Date(System.currentTimeMillis())
            plank.date = date
            plank.sec = getSec()
        }
        Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)
            .setAction("戻る") { findNavController().popBackStack() }
            .setActionTextColor(Color.YELLOW)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.setFabVisible(View.VISIBLE)
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun getSec(): Int {
        return (count / (1000 / period)).toInt()
    }

}