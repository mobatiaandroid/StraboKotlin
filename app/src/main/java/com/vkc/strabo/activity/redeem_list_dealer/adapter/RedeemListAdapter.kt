package com.vkc.strabo.activity.redeem_list_dealer.adapter

import android.app.Activity
import android.content.Context
import com.vkc.strabo.activity.redeem_list_dealer.model.GiftListModel
import android.widget.BaseExpandableListAdapter
import com.vkc.strabo.constants.VKCUrlConstants
import com.vkc.strabo.activity.redeem_list_dealer.model.GiftUserModel
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.vkc.strabo.R
import android.widget.TextView
import java.util.ArrayList

class RedeemListAdapter(
    var mContext: Activity,
    var listGift: ArrayList<GiftListModel>
) : BaseExpandableListAdapter(), VKCUrlConstants {
    var giftUserList: ArrayList<GiftUserModel>? = null
    var positionValue = 0
    override fun getGroupCount(): Int {
        return listGift.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        giftUserList = listGift[groupPosition].listGiftUser
        // return listTransaction.get(positionValue).getListHistory().size();
        return giftUserList!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return giftUserList!![groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return giftUserList!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return 0
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        convertView: View, parent: ViewGroup
    ): View {
        var convertView = convertView
        if (convertView == null) {
            val infalInflater = mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(
                R.layout.item_history_parent,
                null
            )
        }
        val textUser = convertView.findViewById<View>(R.id.textUser) as TextView
        val textPoints = convertView
            .findViewById<View>(R.id.textPoint) as TextView
        val textIcon = convertView.findViewById<View>(R.id.textIcon) as TextView
        textPoints.text = "Mobile :" + listGift[groupPosition].phone
        textUser.text = listGift[groupPosition].name
        positionValue = groupPosition
        if (isExpanded) {
            textIcon.text = "-"
        } else {
            textIcon.text = "+"
        }
        return convertView
    }

    internal class ViewHolder {
        var textGiftType: TextView? = null
        var textGiftQuantity: TextView? = null
        var textGiftName: TextView? = null // ImageView imageGift;
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View, parent: ViewGroup
    ): View {
        /*
		 * System.out.println("Group Position:"+groupPosition+"Child Psoitiomn:"+
		 * childPosition);
		 * System.out.println("Count:"+listTransaction.get(groupPosition
		 * ).getListHistory().size());
		 */
        var viewHolder: ViewHolder? = null
        var v = convertView
        if (convertView == null) {
            val inflater = mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.item_gift, null)
            viewHolder = ViewHolder()
            viewHolder.textGiftName = v.findViewById<View>(R.id.textGiftName) as TextView
            viewHolder.textGiftType = v.findViewById<View>(R.id.textGiftType) as TextView
            viewHolder.textGiftQuantity = v.findViewById<View>(R.id.textGiftQuantity) as TextView
            v.tag = viewHolder
        } else {
            viewHolder = v.tag as ViewHolder
        }

        // viewHolder.imageGift = (ImageView) v.findViewById(R.id.imageGift);

        // childPosition=childPosition-1;
        if (childPosition % 2 == 1) {
            // view.setBackgroundColor(Color.BLUE);
            v.setBackgroundColor(
                mContext.resources.getColor(
                    R.color.list_row_color_grey
                )
            )
        } else {
            v.setBackgroundColor(
                mContext.resources.getColor(
                    R.color.list_row_color_white
                )
            )
        }
        // String date=productList.get(childPosition).getDateValue();
        viewHolder.textGiftName!!.text = giftUserList!![childPosition]
            .gift_title
        viewHolder!!.textGiftType!!.text = giftUserList!![childPosition]
            .gift_type
        viewHolder.textGiftQuantity!!.text = giftUserList!![childPosition]
            .quantity
        /*
		 * if (!giftUserList.get(childPosition).getGift_image().equals("")) {
		 * Picasso.with(mContext)
		 * .load(giftUserList.get(childPosition).getGift_image())
		 * .placeholder(R.drawable.gift).into(viewHolder.imageGift); } else {
		 * 
		 * }
		 */
        // System.out.println("Position Value:"+childPosition);
        return v
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }
}