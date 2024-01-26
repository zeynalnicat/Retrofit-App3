package com.matrix.wishlist.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.matrix.wishlist.databinding.FragmentWishListBinding
import com.matrix.wishlist.db.RoomDb
import com.matrix.wishlist.ui.adapters.WishListAdapter
import com.matrix.wishlist.viewmodel.WishListViewModel
import com.matrix.wishlist.viewmodel.factory.WishListFactory

class WishListFragment : Fragment() {
   private lateinit var binding:FragmentWishListBinding
   private lateinit var roomDb: RoomDb
   private val wishListViewModel: WishListViewModel by viewModels { WishListFactory(roomDb) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishListBinding.inflate(inflater)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomDb = RoomDb.accessDB(requireContext())!!
        wishListViewModel.getProducts()
        setAdapter()


    }

    private fun setAdapter(){
        wishListViewModel.products.observe(viewLifecycleOwner){
            it?.let {
                val adapter =WishListAdapter()
                adapter.submitList(it)
                binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),1)
                binding.recyclerView.adapter = adapter
            }
        }
    }


}