package com.nazarrybickij.juniortesttask.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.nazarrybickij.juniortesttask.entity.Car
import com.nazarrybickij.juniortesttask.R


class Adapter(cars: ArrayList<Car>, var callback: ClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var sectionedCars: List<RecyclerItem>
    private val ITEM_TYPE = 0
    private val TITLE_TYPE = 1
    private val AD_TYPE = 3
//    val spaceBetweenAds = 6
    var adList = mutableListOf<NativeAd>()
    set(value) {
        notifyDataSetChanged()
        field = value
    }

    init {
        val sectionedCars: List<RecyclerItem> = cars
            .groupBy { it.car_make }
            .flatMap { (category, car) ->
                listOf<RecyclerItem>(RecyclerItem.Section(category)) +
                        car.map { RecyclerItem.Value(it) } + RecyclerItem.AD
            }
        this.sectionedCars = sectionedCars
    }

    interface ClickListener {
        fun itemClick(it: Car)
    }

    sealed class RecyclerItem {
        data class Value(val car: Car) : RecyclerItem()
        data class Section(val title: String) : RecyclerItem()
        object AD : RecyclerItem()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        val titleView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_title_item, parent, false)
        val adView = LayoutInflater.from(parent.context)
            .inflate(R.layout.native_ad, parent, false)
        return when (viewType) {
            TITLE_TYPE -> TitleViewHolder(titleView)
            ITEM_TYPE -> ItemViewHolder(itemView)
            AD_TYPE -> AdViewHolder(adView)
            else -> ItemViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TITLE_TYPE -> (holder as TitleViewHolder).onBind(position)
            ITEM_TYPE -> (holder as ItemViewHolder).onBind(position)
            AD_TYPE -> (holder as AdViewHolder).onBind()
        }
    }

    override fun getItemViewType(position: Int): Int {
//        if (position % (spaceBetweenAds + 1) == spaceBetweenAds) {
//            return AD_TYPE
//        }
        return when (sectionedCars[position]) {
            is RecyclerItem.Value -> ITEM_TYPE
            is RecyclerItem.Section -> TITLE_TYPE
            is RecyclerItem.AD -> AD_TYPE
        }
    }

    override fun getItemCount() = sectionedCars.size

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private var root: ConstraintLayout = itemView.findViewById(R.id.root_item)
        private var nameTxt: TextView = itemView.findViewById(R.id.nameTxt)
        private var yearTxt: TextView = itemView.findViewById(R.id.yearTxt)
        private var image: ImageView = itemView.findViewById(R.id.image)

        init {
            itemView.setOnClickListener(this)
        }

        fun onBind(position: Int) {
            val value = sectionedCars[position] as RecyclerItem.Value
            val car = value.car
            nameTxt.text = "${car.car_make} ${car.car_model}"
            yearTxt.text = car.car_model_year.toString()
            Glide.with(itemView.context).load(car.image).into(image)
        }

        override fun onClick(v: View?) {
            when (v) {
                root -> {
                    val car = sectionedCars[layoutPosition] as RecyclerItem.Value
                    callback.itemClick(car.car)
                }
            }
        }

    }

    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var text: TextView = itemView.findViewById(R.id.section_text)
        fun onBind(position: Int) {
            val a = sectionedCars[position] as RecyclerItem.Section
            text.text = a.title
        }
    }

    inner class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var adNativeView: NativeAdView = itemView.findViewById(R.id.my_template)
        var adName: TextView = itemView.findViewById(R.id.ad_nameTxt)
        var adImage: ImageView = itemView.findViewById(R.id.ad_image)

        fun onBind() {
            if (adList.size != 0) {
                val nativeAd = adList.random()
                if (nativeAd.icon != null) {
                    Glide.with(itemView.context).load(nativeAd.icon.uri).into(adImage)
                }
                adName.text = nativeAd.headline
                adNativeView.headlineView = adName
                adNativeView.setNativeAd(nativeAd)
            }
        }
    }

}