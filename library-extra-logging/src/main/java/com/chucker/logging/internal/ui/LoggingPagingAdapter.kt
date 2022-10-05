package com.chucker.logging.internal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chucker.logging.databinding.ChuckerItemLogBinding
import com.chucker.logging.internal.data.entity.LogData
import com.chucker.logging.internal.support.formatDate
import com.chucker.logging.internal.support.formatLog
import org.json.JSONObject
import java.text.SimpleDateFormat

internal class LoggingPagingAdapter(private val copyCallback: (LogViewParam) -> Unit) :
    PagingDataAdapter<LogData, LoggingPagingAdapter.ViewHolder>(DiffCallback) {

    internal class ViewHolder(
        private val binding: ChuckerItemLogBinding,
        private val copyCallback: (LogViewParam) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(value: LogData) {
            val logViewParam = LogViewParam(
                tag = value.tag,
                logText = value.logString.formatLog(),
                dateText = value.timeStamp.formatDate()
            )

            binding.tag.text = logViewParam.tag
            binding.message.text = logViewParam.logText
            binding.timeStamp.text = logViewParam.dateText
            binding.copyButton.setOnClickListener {
                copyCallback.invoke(logViewParam)
            }
        }
    }

    internal object DiffCallback : DiffUtil.ItemCallback<LogData>() {
        override fun areItemsTheSame(oldItem: LogData, newItem: LogData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: LogData, newItem: LogData): Boolean {
            return oldItem == newItem
        }

        // Overriding function is empty on purpose to avoid flickering by default animator
        override fun getChangePayload(oldItem: LogData, newItem: LogData) = Unit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ChuckerItemLogBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            copyCallback
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.run(holder::bind)
    }
}