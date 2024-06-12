package com.name.a16androidkotlinfirebaseinstagram.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.name.a16androidkotlinfirebaseinstagram.R
import com.name.a16androidkotlinfirebaseinstagram.adapter.FeedRecyclerAdapter
import com.name.a16androidkotlinfirebaseinstagram.databinding.ActivityFeedBinding
import com.name.a16androidkotlinfirebaseinstagram.model.Post

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var feedAdapter : FeedRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        //firebase de iki adet veri okuma secenegi vaar birisi surekli guncelleniyor ki mesajlar aninda iletilisin bir digeri ise bir kere veriyi cekiyor ve
        //senin belirledigin vakitlerde veya belli aralaiklara da verilyi ceekbiliyorusn.
        //chat uygulamairinda real time lar kullanilirken senin dusundugun uygulamda ise get data methodu veya kod yapisi kullanilebilir ama biz bu uygulamda
        //real time kod yapisini kullanacagz

        //https://firebase.google.com/docs/firestore/query-data/get-data?hl=en&authuser=0    burdaki Get a document bolumu dusundugumuz uygualmda isimizi gorebilir.
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth= Firebase.auth

        db=Firebase.firestore
        postArrayList= ArrayList<Post>()

        getdata()
        binding.recyclerView.layoutManager= LinearLayoutManager(this)
        feedAdapter=FeedRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter=feedAdapter

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getdata() {
        db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (value!=null){
                    if(!value.isEmpty){
                        val documents=value.documents
                        postArrayList.clear()
                        for (document in documents){
                            val comment=document.get("comment") as String
                            val userEmail=document.get("userEmail") as String
                            val downloadUrl=document.get("downloadedUrl") as String

                            val post=Post(userEmail,comment,downloadUrl)
                            postArrayList.add(post)

                        }

                        feedAdapter.notifyDataSetChanged()

                    }

                }
            }




        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater= menuInflater
        menuInflater.inflate(R.menu.insta_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.add_post){
            val intent = Intent (this, UploadActivity::class.java)
            startActivity(intent)
        } else if (item.itemId== R.id.signout) {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }











}