package com.thedramaticcolumnist.appdistributor.ui.BottomSheet.confirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thedramaticcolumnist.appdistributor.DataBase.mDatabase.mProducts
import com.thedramaticcolumnist.appdistributor.R
import com.thedramaticcolumnist.appdistributor.Utils.mUtils.mToast
import com.thedramaticcolumnist.appdistributor.databinding.DeleteConfirmationBinding


class DeleteConfirmation(private val productID: String) : BottomSheetDialogFragment(),
    View.OnClickListener {

    private var _binding: DeleteConfirmationBinding? = null
    private val bind get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DeleteConfirmationBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllComponent()
    }

    private fun initAllComponent() {
        bind.cancel.setOnClickListener(this)
        bind.delete.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.delete -> {
                mProducts.child(productID).removeValue()
                    .addOnFailureListener { mToast(requireContext(), it.message.toString()) }
                    .addOnSuccessListener {
                        mToast(requireContext(), "Deleted !! ")
                        dismiss()
                    }
            }
            R.id.cancel -> {
                dismiss()
            }
        }
    }
}