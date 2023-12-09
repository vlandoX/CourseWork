package com.example.coursework.screens.passenger.routeListing

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.databinding.RawRouteItemLayoutBinding
import com.example.coursework.retrofit.TrackExpanded

class RouteRecyclerAdapter(
): RecyclerView.Adapter<RouteRecyclerAdapter.RouteViewHolder>() {
    inner class RouteViewHolder(private val rawRouteItemLayoutBinding: RawRouteItemLayoutBinding) :
        RecyclerView.ViewHolder(rawRouteItemLayoutBinding.root) {

        fun bind(track:TrackExpanded){

            rawRouteItemLayoutBinding.apply {
                departureTimeTextView.text = track.departureTime
                maxSeatsTextView.text = track.maxSeats.toString()
                nameDriverTextView.text = track.driverName
                commentTextView.text = track.driverComment
                fromTextView.text = track.startLocation.address
                toTextView.text = track.endLocation.address
                root.setOnClickListener {
                    onClickListener?.onClick(track)
                }
            }



        }

    }

    private var onClickListener: OnClickListener? = null

    private val diffCallback = object : DiffUtil.ItemCallback<TrackExpanded>()
    {
        override fun areItemsTheSame(oldItem: TrackExpanded, newItem: TrackExpanded): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TrackExpanded, newItem: TrackExpanded): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var result : List<TrackExpanded>
        get() = differ.currentList
        set(value){
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        return RouteViewHolder(RawRouteItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.bind(result[position])
    }

    override fun getItemCount() = result.size


    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(track: TrackExpanded)
    }
}