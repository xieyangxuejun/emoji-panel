package com.foretree.support.emoji.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by silen on 16/08/2018
 */
abstract class BaseListAdapter<T, VH : RecyclerView.ViewHolder> : ListAdapter<T, VH>(BaseDiffCallback<T>()) {
    protected var mOnItemClickListener: ((adapter: BaseListAdapter<T, VH>, view: View, position: Int) -> Unit)? = null
    protected var mItemLongClickListener: ((adapter: BaseListAdapter<T, VH>, view: View, position: Int) -> Boolean)? = null

    override fun onBindViewHolder(holder: VH, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            this.onBindViewHolder(holder, position)
        } else {
            this.onBindViewHolder(holder, position, getItem(position), payloads[0])
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        this.onBindViewHolder(holder, position, getItem(position))
        if (holder.itemView != null) {
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(OnItemClickListenerImpl(this, position))
            }
            if (mItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(OnItemLongClickListenerImpl(this, position))
            }
        }
    }

    public override fun getItem(position: Int): T {
        return super.getItem(position)
    }

    private fun onBindViewHolder(holder: VH, position: Int, item: T?, payload: Any) {
        onBindViewHolder(holder, position, item)
    }

    abstract fun onBindViewHolder(holder: VH, position: Int, item: T?)

    fun setOnItemClickListener(onItemClickListener: (adapter: BaseListAdapter<T, VH>, view: View, position: Int) -> Unit) {
        this.mOnItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: (adapter: BaseListAdapter<T, VH>, view: View, position: Int) -> Boolean) {
        this.mItemLongClickListener = onItemLongClickListener
    }

    private class OnItemClickListenerImpl<T, VH : RecyclerView.ViewHolder>(private val mAdapter: BaseListAdapter<T, VH>, private val mPosition: Int) : View.OnClickListener {

        override fun onClick(v: View) {
            if (mAdapter.mOnItemClickListener == null) return
            mAdapter.mOnItemClickListener?.apply { this(mAdapter, v, mPosition) }
        }
    }

    private class OnItemLongClickListenerImpl<T, VH : RecyclerView.ViewHolder>(private val mAdapter: BaseListAdapter<T, VH>, private val mPosition: Int) : View.OnLongClickListener {

        override fun onLongClick(v: View): Boolean {
            if (mAdapter.mOnItemClickListener == null) return false
            return mAdapter.mItemLongClickListener!!.let { it(mAdapter, v, mPosition) }
        }
    }
}

class BaseListViewHolder(
        parent: ViewGroup, contentViewId: Int
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(contentViewId, parent, false))

class BaseDiffCallback<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}

