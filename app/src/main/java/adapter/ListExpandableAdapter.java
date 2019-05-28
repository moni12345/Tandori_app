package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import activity.ItemFromActivty;
import model.AddbasketBeanFromHome;
import model.Items;
import util.GlobalClass;


public class ListExpandableAdapter extends BaseExpandableListAdapter {


    public static int count = 0;
    public static ArrayList<AddbasketBeanFromHome> basketarr = new ArrayList<>();
    static int simlpleitemCheck;
    List<Items> header;
    HashMap<String, List<Items>> hashMap;
    Activity act;
    ImageButton btnMinusSub;
    ImageView ivGoChoice;
    TextView txtListChild, txtsubItemPrice, txtsubItemId;
    Items it;
    String branch_ID;
    View ActivityView;

    public ListExpandableAdapter(Activity act, List<Items> list, HashMap<String, List<Items>> hlist, String branchID, View v) {

        this.act = act;
        header = list;
        hashMap = hlist;
        this.branch_ID = branchID;
        ActivityView = v;
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return this.header.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return this.hashMap.get(this.header.get(groupPosition).getCat_name()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        Items cat = new Items();
        cat.setCat_name(header.get(groupPosition).getCat_name());


        return cat;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        // ArrayList<Items> chList = header.get(groupPosition).g;
        Items chList2 = this.hashMap.get(this.header.get(groupPosition).getCat_name()).get(
                childPosition);
        return chList2;
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LinearLayout ll = null;
        String item = header.get(groupPosition).getCat_name();
        String itemDesc = header.get(groupPosition).getCat_desc();

        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) this.act
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);

        }
        ImageView cellArrowImage = (ImageView) convertView.findViewById(R.id.arrow_image);

        ll = (LinearLayout) convertView.findViewById(R.id.thumbnail);
        System.out.println("Convertview class one" + convertView);
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);

        //tvDesc.setText(itemDesc);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(item);

        if (isExpanded) {
            // convertView.setBackgroundResource(R.drawable.cell_red);

            //change backgroung when group expanded ....
            ll.setBackgroundResource(R.color.black);
            cellArrowImage.setImageResource(R.drawable.ic_keyboard_arrow_up_white_24dp);

            // change into Description
            if (itemDesc.equals("-")) {
                lblListHeader.setTextColor(act.getResources().getColor(R.color.white));
                lblListHeader.setText(item);
            } else {
                lblListHeader.setText(itemDesc);
                lblListHeader.setTextColor(act.getResources().getColor(R.color.white));
                lblListHeader.setTypeface(null, Typeface.NORMAL);
            }
        } else {

            ll.setBackgroundResource(R.color.cell_color);
            cellArrowImage.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            lblListHeader.setTextColor(act.getResources().getColor(R.color.white));
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // final String subItemText = null;

        it = (Items) getChild(groupPosition, childPosition);

        LayoutInflater infalInflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.list_subitem, null);
        final View v2 = convertView;


        txtListChild = (TextView) convertView.findViewById(R.id.lblListSubItem);

        ivGoChoice = (ImageView) convertView.findViewById(R.id.ivgpChoice);
        btnMinusSub = (ImageButton) convertView
                .findViewById(R.id.imgBtnMinusIDSub);

        ImageButton btnPlusSub = (ImageButton) convertView
                .findViewById(R.id.imgBtnPlusIDSub);

        final TextView txtSubItemCount = (TextView) convertView
                .findViewById(R.id.txtViewPizzaCountIDSub);

        txtsubItemPrice = (TextView) convertView
                .findViewById(R.id.txtViewPriceID);

        TextView txtsubItemPriceDesc = (TextView) convertView
                .findViewById(R.id.txtViewsubDetail);

        txtsubItemId = (TextView) convertView.findViewById(R.id.tvSubItemId);


        txtsubItemId.setText(it.getItemID());
        txtListChild.setText(it.getItemName());
        txtsubItemPriceDesc.setText(it.getItem_Desc());

        txtsubItemPrice.setText(it.getItemPrice());


        txtSubItemCount.setText(it.getQuantity());


        String offerchoice = it.getOfferChoices();

        // idsArr.add(itemid);
        if (it.getItemPrice().equals("0.00") && !offerchoice.equals("1")) {
            TextView poundTextView = (TextView) convertView.findViewById(R.id.tvPoundSign);
            poundTextView.setVisibility(View.INVISIBLE);
            txtsubItemPrice.setVisibility(View.INVISIBLE);
        }

        if (offerchoice.equals("1")) {
            btnMinusSub.setVisibility(View.INVISIBLE);
            btnPlusSub.setVisibility(View.INVISIBLE);
            txtSubItemCount.setVisibility(View.INVISIBLE);
            ivGoChoice.setVisibility(View.VISIBLE);

            txtListChild.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    TextView tvItemName = (TextView) v2
                            .findViewById(R.id.lblListSubItem);
                    TextView tvPrice = (TextView) v2
                            .findViewById(R.id.txtViewPriceID);
                    TextView tvitemID = (TextView) v2
                            .findViewById(R.id.tvSubItemId);
                    TextView tvDesc = (TextView) v2
                            .findViewById(R.id.txtViewsubDetail);
                    GlobalClass.setCircleCountValuePlus(ActivityView);

                    String itemName = tvItemName.getText().toString();
                    String itemPrice = tvPrice.getText().toString();
                    String itemID = tvitemID.getText().toString();
                    String desc = tvDesc.getText().toString();


                    Intent i = new Intent(act, ItemFromActivty.class);
                    i.putExtra("Name", itemName);
                    i.putExtra("price", itemPrice);
                    i.putExtra("itemID", itemID);
                    i.putExtra("branch_ID", branch_ID);
                    i.putExtra("desc", desc);

                    act.startActivity(i);

                }
            });
            txtsubItemPriceDesc.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    TextView tvItemName = (TextView) v2
                            .findViewById(R.id.lblListSubItem);
                    TextView tvPrice = (TextView) v2
                            .findViewById(R.id.txtViewPriceID);
                    TextView tvitemID = (TextView) v2
                            .findViewById(R.id.tvSubItemId);
                    TextView tvDesc = (TextView) v2
                            .findViewById(R.id.txtViewsubDetail);
                    GlobalClass.setCircleCountValuePlus(ActivityView);

                    String itemName = tvItemName.getText().toString();
                    String itemPrice = tvPrice.getText().toString();
                    String itemID = tvitemID.getText().toString();
                    String desc = tvDesc.getText().toString();


                    Intent i = new Intent(act, ItemFromActivty.class);
                    i.putExtra("Name", itemName);
                    i.putExtra("price", itemPrice);
                    i.putExtra("itemID", itemID);
                    i.putExtra("branch_ID", branch_ID);
                    i.putExtra("desc", desc);

                    act.startActivity(i);
                }
            });


            ivGoChoice.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    TextView tvItemName = (TextView) v2
                            .findViewById(R.id.lblListSubItem);
                    TextView tvPrice = (TextView) v2
                            .findViewById(R.id.txtViewPriceID);
                    TextView tvitemID = (TextView) v2
                            .findViewById(R.id.tvSubItemId);
                    TextView tvDesc = (TextView) v2
                            .findViewById(R.id.txtViewsubDetail);
                    GlobalClass.setCircleCountValuePlus(ActivityView);

                    String itemName = tvItemName.getText().toString();
                    String itemPrice = tvPrice.getText().toString();
                    String itemID = tvitemID.getText().toString();
                    String desc = tvDesc.getText().toString();

                    // Toast.makeText(act, "" + itemID, 0).show();

                    Intent i = new Intent(act, ItemFromActivty.class);
                    i.putExtra("Name", itemName);
                    i.putExtra("price", itemPrice);
                    i.putExtra("itemID", itemID);
                    i.putExtra("branch_ID", branch_ID);
                    i.putExtra("desc", desc);

                    act.startActivity(i);
                }
            });
        }

        // final int count = 0;
        btnPlusSub.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                simlpleitemCheck++;

                AddbasketBeanFromHome bean = new AddbasketBeanFromHome();

                TextView tvItemName = (TextView) v2
                        .findViewById(R.id.lblListSubItem);
                TextView tvPrice = (TextView) v2
                        .findViewById(R.id.txtViewPriceID);

                TextView tvitemID = (TextView) v2
                        .findViewById(R.id.tvSubItemId);
                TextView tvDesc = (TextView) v2
                        .findViewById(R.id.txtViewsubDetail);
                TextView itemQuantity = (TextView) v2
                        .findViewById(R.id.txtViewPizzaCountIDSub);

                String counter = itemQuantity.getText().toString();

                count++;
                GlobalClass.setCircleCountValuePlus(ActivityView);

                int qty = Integer.parseInt(counter);

                qty++;

                itemQuantity.setText("" + qty);

                try {
                    for (int j = 0; j < basketarr.size(); j++) {

                        if (basketarr.get(j).getID()
                                .equals(tvitemID.getText().toString())) {
                            if (GlobalClass.globalBasketList.get(j).getID()
                                    .equals(tvitemID.getText().toString())) {

                                GlobalClass.globalBasketList.get(j).setQuantity(
                                        "" + qty);
                            }
                            basketarr.get(j).setQuantity("" + qty);
                            return;
                        }
                    }

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                bean.setItemName(tvItemName.getText().toString());
                bean.setItemPrice(tvPrice.getText().toString());
                bean.setID(tvitemID.getText().toString());
                bean.setDesc(tvDesc.getText().toString());
                bean.setQuantity("" + qty);
                bean.setItemType("i");
                bean.setItemWine("hitem");
                basketarr.add(bean);
                GlobalClass.globalBasketList.add(bean);

            }

        });
        btnMinusSub.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (simlpleitemCheck > 0)
                    simlpleitemCheck--;


                TextView tvitemID = (TextView) v2
                        .findViewById(R.id.tvSubItemId);

                TextView itemQuantity = (TextView) v2
                        .findViewById(R.id.txtViewPizzaCountIDSub);
                String counter = itemQuantity.getText().toString();
                if (!counter.equals("0")) {
                    count--;
                    GlobalClass.setCircleCountValueMinus(ActivityView);
                    int qty = Integer.parseInt(counter);

                    qty--;

                    itemQuantity.setText("" + qty);
                    for (int j = 0; j < basketarr.size(); j++) {

                        if (basketarr.get(j).getID()
                                .equals(tvitemID.getText().toString())) {
                            basketarr.get(j).setQuantity("" + qty);
                            if (basketarr.get(j).getQuantity().equals("0")) {
                                basketarr.remove(j);
                                GlobalClass.globalBasketList.remove(j);
                            }
                            return;
                        }
                    }

                }
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

}
