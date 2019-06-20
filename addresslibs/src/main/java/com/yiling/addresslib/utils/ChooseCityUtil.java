package com.yiling.addresslib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yiling.addresslib.R;
import com.yiling.addresslib.bean.CityBean;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/*
 * Description:
 * Author: song
 * Time:  2018/3/9 10:26
 */
public class ChooseCityUtil {

    private Context context;
    private ChooseCityInterface cityInterface;
    private TextView tv_address1, tv_address2, tv_address3, tv_done;
    private View view1, view2, view3;
    private String[] newCityArray = new String[3];
    private PopupWindow checkOutWindow;
    private CityBean bean;
    private RecyclerView recycler_view;

    public void createDialog(Context context, ChooseCityInterface cityInterface) {
        this.context = context;
        this.cityInterface = cityInterface;

        Gson gson = new Gson();
        bean = gson.fromJson(getJson("address.json"), CityBean.class);

        showCheckOutPopWindow();
    }

    private String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assets = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assets.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void showCheckOutPopWindow() {
        View convertview = View.inflate(context, R.layout.dialog_city, null);
        tv_done = convertview.findViewById(R.id.tv_done);
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOutWindow.dismiss();
                cityInterface.sure(newCityArray);
            }
        });
        //取消和退出的监听
        tv_address1 = (TextView) convertview.findViewById(R.id.tv_address1);
        tv_address2 = (TextView) convertview.findViewById(R.id.tv_address2);
        tv_address3 = (TextView) convertview.findViewById(R.id.tv_address3);

        view1 = convertview.findViewById(R.id.view1);
        view2 = convertview.findViewById(R.id.view2);
        view3 = convertview.findViewById(R.id.view3);

        recycler_view = convertview.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        final MyAdapter adapter = new MyAdapter();
        recycler_view.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (currentGrade == 0) {
                    firstPosition = position;
                    tv_address1.setText(bean.getList().get(position).getName());
                    newCityArray[0] = bean.getList().get(position).getName();

                    newCityArray[1] = bean.getList().get(position).getCityList().get(0).getName();

                    try {
                        newCityArray[2] = bean.getList().get(position).getCityList().get(0).getAreaList().get(0).getName();
                    } catch (Exception e) {
                        newCityArray[2] = "无";
                    }

                    view1.setVisibility(View.GONE);

                    tv_address2.setVisibility(View.VISIBLE);
                    tv_address2.setText("请选择");
                    view2.setVisibility(View.VISIBLE);

                    tv_address3.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);

                    currentGrade = 1;
                    secondPosition = -1;
                    thirdPosition = -1;
                } else if (currentGrade == 1) {
                    secondPosition = position;
                    tv_address2.setText(bean.getList().get(firstPosition).getCityList().get(position).getName());
                    newCityArray[1] = bean.getList().get(firstPosition).getCityList().get(position).getName();

                    try {
                        newCityArray[2] = bean.getList().get(firstPosition).getCityList().get(secondPosition).getAreaList().get(0).getName();
                    } catch (Exception e) {
                        newCityArray[2] = "无";
                    }
                    view2.setVisibility(View.GONE);

                    tv_address3.setVisibility(View.VISIBLE);
                    tv_address3.setText("请选择");
                    view3.setVisibility(View.VISIBLE);

                    currentGrade = 2;
                    thirdPosition = -1;
                } else if (currentGrade == 2) {
                    thirdPosition = position;
                    if (bean.getList().get(firstPosition).getCityList().get(secondPosition).getAreaList().size() == 0) {
                        tv_address3.setText("无");
                    } else {
                        tv_address3.setText(bean.getList().get(firstPosition).getCityList().get(secondPosition).getAreaList().get(position).getName());
                    }
                    try {
                        newCityArray[2] = bean.getList().get(firstPosition).getCityList().get(secondPosition).getAreaList().get(position).getName();
                    } catch (Exception e) {
                        newCityArray[2] = "无";
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        tv_address1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentGrade = 0;
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                recycler_view.scrollToPosition(firstPosition);
                LinearLayoutManager mLayoutManager =
                        (LinearLayoutManager) recycler_view.getLayoutManager();
                mLayoutManager.scrollToPositionWithOffset(firstPosition, 0);
            }
        });

        tv_address2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentGrade = 1;
                view2.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                recycler_view.scrollToPosition(secondPosition);
                LinearLayoutManager mLayoutManager =
                        (LinearLayoutManager) recycler_view.getLayoutManager();
                mLayoutManager.scrollToPositionWithOffset(secondPosition, 0);
            }
        });

        tv_address3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentGrade = 2;
                view3.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                recycler_view.scrollToPosition(thirdPosition);
                LinearLayoutManager mLayoutManager =
                        (LinearLayoutManager) recycler_view.getLayoutManager();
                mLayoutManager.scrollToPositionWithOffset(thirdPosition, 0);
            }
        });


        checkOutWindow = new PopupWindow(convertview, ViewGroup.LayoutParams.MATCH_PARENT, -2, true);
        checkOutWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        checkOutWindow.showAtLocation(tv_address1, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.5f);
        checkOutWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                firstPosition = -1;
                secondPosition = -1;
                thirdPosition = -1;
                currentGrade = 0;
                backgroundAlpha(1.0f);
            }
        });
        checkOutWindow.setAnimationStyle(R.style.popwindow_anim_style);
        checkOutWindow.update();
    }

    /**
     * 设置透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        Activity act = (Activity) context;
        WindowManager.LayoutParams lp = act.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        act.getWindow().setAttributes(lp);
    }

    int firstPosition = -1;
    int secondPosition = -1;
    int thirdPosition = -1;
    int currentGrade = 0;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_textview, parent, false);
            ViewHolder holder = new ViewHolder(view);
            holder.item = view.findViewById(R.id.tv_item);
            holder.chosen = view.findViewById(R.id.iv_chosen);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if (currentGrade == 0) {
                holder.item.setText(bean.getList().get(position).getName());
                if (firstPosition == position) {
                    holder.item.setTextColor(Color.parseColor("#FF5757"));
                    holder.chosen.setVisibility(View.VISIBLE);
                } else {
                    holder.item.setTextColor(Color.parseColor("#000000"));
                    holder.chosen.setVisibility(View.GONE);
                }
            } else if (currentGrade == 1) {
                holder.item.setText(bean.getList().get(firstPosition).getCityList().get(position).getName());
                if (secondPosition == position) {
                    holder.item.setTextColor(Color.parseColor("#FF5757"));
                    holder.chosen.setVisibility(View.VISIBLE);
                } else {
                    holder.item.setTextColor(Color.parseColor("#000000"));
                    holder.chosen.setVisibility(View.GONE);
                }
            } else if (currentGrade == 2) {
                if (bean.getList().get(firstPosition).getCityList().get(secondPosition).getAreaList().size() == 0) {
                    holder.item.setText("无");
                } else {
                    holder.item.setText(bean.getList().get(firstPosition).getCityList().get(secondPosition).getAreaList().get(position).getName());
                }
                if (thirdPosition == position) {
                    holder.item.setTextColor(Color.parseColor("#FF5757"));
                    holder.chosen.setVisibility(View.VISIBLE);
                } else {
                    holder.item.setTextColor(Color.parseColor("#000000"));
                    holder.chosen.setVisibility(View.GONE);
                }
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (currentGrade == 0) {
                return bean.getList().size();
            } else if (currentGrade == 1) {
                return bean.getList().get(firstPosition).getCityList().size();
            } else if (currentGrade == 2) {
                if (bean.getList().get(firstPosition).getCityList().get(secondPosition).getAreaList().size() == 0) {
                    return 1;
                } else {
                    return bean.getList().get(firstPosition).getCityList().get(secondPosition).getAreaList().size();
                }
            }
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }

            TextView item;
            ImageView chosen;
        }
    }
}