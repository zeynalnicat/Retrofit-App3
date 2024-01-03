package com.matrix.android_104_android.ui.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.matrix.android_104_android.R
import com.matrix.android_104_android.databinding.ItemSingleProductBinding
import com.matrix.android_104_android.db.RoomDb
import com.matrix.android_104_android.db.WishListEntity
import com.matrix.android_104_android.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductAdapter(private val context: Context, private val nav: (Bundle) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    private val diffUtil = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemSingleProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(diffUtil.currentList[position])
    }

    inner class ViewHolder(private val binding: ItemSingleProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(current: Product) {
            Glide.with(binding.root).load(current.thumbnail).into(binding.imgProduct)
            binding.txtProductName.text = current.title
            CoroutineScope(Dispatchers.Main).launch {
                binding.imgLike.setImageResource(if (checkInDb(current.id)) R.drawable.icon_heart_filled else R.drawable.icon_heart)
            }

            binding.txtProductPrice.text = "$" + "${current.price}"
            binding.imgLike.setOnClickListener {
                insertDb(current)
                notifyItemChanged(layoutPosition)

            }
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("product", current)
                nav(bundle)
            }
        }
        private suspend fun checkInDb(id: Int): Boolean {
            val roomDb = RoomDb.accessDB(context.applicationContext)
            val wishListDao = roomDb?.wishListDao()
            val check = wishListDao?.checkInDb(id)
            return check?.isNotEmpty() ?: false
        }

        fun insertDb(current: Product) {
            val roomDb = RoomDb.accessDB(context.applicationContext)
            val wishListDao = roomDb?.wishListDao()
            CoroutineScope(Dispatchers.IO).launch {
                if (!checkInDb(current.id)) {
                    val insertion = wishListDao?.insert(
                        WishListEntity(
                            brand = current.brand,
                            thumbnail = current.thumbnail,
                            title = current.title,
                            productId = current.id,
                            price = current.price,
                            stock = current.stock,
                            description = current.description,
                            discountPercentage = current.discountPercentage,
                            category = current.category,
                            rating = current.rating,
                        )
                    )
                } else {
                    val wishId = wishListDao?.getId(current.id)
                    wishListDao?.remove(wishId!!)

                }
            }
        }
    }

    fun submitList(products: List<Product>) {
        diffUtil.submitList(products)
    }
}