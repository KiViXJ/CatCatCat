package com.example.catcatcat

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.red
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity() {
    fun randomColor(): Int {
        return Color.argb(50, Random.nextInt(0, 256), Random.nextInt(0, 256), Random.nextInt(0, 256))
    }
    fun savePets(pets: Int) {
        openFileOutput("pets.txt", MODE_PRIVATE).use { fos ->
            fos.write(pets.toString().toByteArray())
        }
    }
    fun loadPets(): Number {
        return try {
            openFileInput("pets.txt").bufferedReader().use { it.readText().toInt() }
        } catch (e: Exception) {
            0; }
    }
    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    fun playSound(soundId: Int) {
        val mediaPlayer = MediaPlayer.create(this, soundId)
        mediaPlayer.setOnCompletionListener { it.release() }
        mediaPlayer.start()
    }
    fun spawnHeart(layout: FrameLayout) {
        val heartImageView = ImageView(this)
        heartImageView.setImageResource(R.drawable.heart)
        heartImageView.scaleType = ImageView.ScaleType.FIT_XY

        val params = FrameLayout.LayoutParams(150, 150)
        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM

        if (Random.nextBoolean()) params.leftMargin = Random.nextInt(0, 501)
        else params.rightMargin = Random.nextInt(0, 501)

        heartImageView.layoutParams = params
        layout.addView(heartImageView)

        val anim = heartImageView.animate()
            .translationY(-2950*3f)
            .setDuration(2500)

        anim.withEndAction { layout.removeView(heartImageView) }
        anim.start()
    }
    fun getPetFace(pets: Int): String {
        if (pets > 2000) return " ;)"
        if (pets > 500) return " :p"
        if (pets > 100) return " :>"
        if (pets > 25) return " :0"
        return " :)"
    }
    fun saturateCat(cat: ImageView) {
        cat.setColorFilter(randomColor())
    }
    @SuppressLint("SetTextI18n")
    fun addPet(counter: TextView) {

        val mainLayout = findViewById<ConstraintLayout>(R.id.main)
        val petsString = counter.text.toString()
        if (petsString == "No pets :(") { counter.text = "1" + " :)"; savePets(1); return }

        val newPets = petsString.substring(0, petsString.length - 3).toInt() + 1

        if (newPets == 666) { playSound(R.raw.hiss); mainLayout.setBackgroundColor("#ff0000".toColorInt()) }
        else { playSound(R.raw.meow) ;mainLayout.setBackgroundColor("#B9FF48".toColorInt()) }

        counter.text = newPets.toString() + getPetFace(newPets)

        savePets(newPets)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val layout = findViewById<FrameLayout>(R.id.mainLayout)
        val petCounter = findViewById<TextView>(R.id.petCounter)
        val petsString = loadPets().toString()

        if (petsString != "0") { petCounter.text = petsString + getPetFace(petsString.toInt()) };

        findViewById<LinearLayout>(R.id.catClick).setOnClickListener {
            saturateCat(findViewById(R.id.cat))
            spawnHeart(layout)
            addPet(petCounter)
        }
    }
}