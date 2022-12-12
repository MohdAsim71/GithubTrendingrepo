package com.codinglance.githubtrendingrepo.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codinglance.githubtrendingrepo.R
import com.codinglance.githubtrendingrepo.animation.MyBounceInterpolator
import com.codinglance.githubtrendingrepo.databinding.RepoListBinding
import com.codinglance.githubtrendingrepo.model.Item
import com.codinglance.githubtrendingrepo.ui.view.MainActivity


class RepoAdapter(private var mainActivity: MainActivity, private  var repoList: ArrayList<Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class RepoViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var binding: RepoListBinding? = DataBindingUtil.bind(v)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.repo_list, parent, false)
        return RepoViewHolder(view)


    }

    override fun getItemCount() =repoList.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        val viewHolder= holder as RepoViewHolder


        val animation = AnimationUtils.loadAnimation(mainActivity, R.anim.button_animation)
        val interpolator = MyBounceInterpolator(0.1, 20.0)
        animation.interpolator = interpolator
        holder.itemView.animation = animation

        Glide.with(mainActivity)
            .load(repoList[position].owner.avatar_url)
            .into(viewHolder.binding!!.repoOwnr);

           viewHolder.binding!!.repoOwnrName.text=repoList[position].name
           viewHolder.binding!!.repoDesc.text=repoList[position].description

        holder.itemView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(repoList[position].html_url))
            mainActivity.startActivity(browserIntent)
        }



    }


    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Item>) {
        repoList = filteredList
        notifyDataSetChanged()
    }
}