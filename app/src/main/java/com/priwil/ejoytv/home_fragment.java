package com.priwil.ejoytv;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by SURYA on 2/1/2018.
 */




public class home_fragment extends Fragment {
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    ArrayList<String> links=new ArrayList<>();
    private TextView[] dots;
    int page_position = 0;


   // private FirebaseDatabase database;
    DatabaseReference myRef;
  //  DatabaseReference myRef_featured;
    private RecyclerView mBlogList;
    private int columns=2;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_home, container, false);
        mBlogList=(RecyclerView)view.findViewById(R.id.blog_list);
        mBlogList.setLayoutManager(new GridLayoutManager(getActivity(),columns));



        FirebaseApp.initializeApp(getActivity());
// method for initialisation
        init(view);

// method for adding indicators
        addBottomDots(0);

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_image_list.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                vp_slider.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 5000);

    return view;
    }

    private void init(View view) {


        vp_slider = (ViewPager) view.findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) view.findViewById(R.id.ll_dots);

        slider_image_list = new ArrayList<>();

//Add few items to slider_image_list ,this should contain url of images which should be displayed in slider
// here i am adding few sample image links, you can add your own

        slider_image_list.add("http://www.wallpaperbetter.com/wallpaper/227/660/902/church-1080P-wallpaper-middle-size.jpg");
        slider_image_list.add("https://www.hdwallpapers.in/walls/catholic_church_vatican-HD.jpg");
        slider_image_list.add("https://images4.alphacoders.com/690/690392.jpg");
        slider_image_list.add("https://images.alphacoders.com/440/440999.jpg");


        sliderPagerAdapter = new SliderPagerAdapter(getActivity(), slider_image_list);//check;
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());//check
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(45);
            dots[i].setTextColor(Color.parseColor("#757575"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#8E24AA"));
    }





    @Override
    public void onStart()
    {
      DatabaseReference  myRef_featured=FirebaseDatabase.getInstance().getReference();

        super.onStart();
        FirebaseRecyclerAdapter<getsetimage, BlogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<getsetimage, BlogViewHolder>(
                getsetimage.class,
                R.layout.desgin_row,
                BlogViewHolder.class,
                myRef_featured)
        {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, getsetimage model,int position)
            {
                final String post_key=getRef(position).getKey();


                viewHolder.setImage(getActivity(), model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                });
            }
        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public BlogViewHolder(View itemView)
        {
            super(itemView);
            mView=itemView;

        }


        public void setImage(Context ctx, String image)
        {
            ImageView post_image = (ImageView) mView.findViewById(R.id.imageViewy);
            //We pass context
            Picasso.with(ctx).load(image).into(post_image);
        }

    }


}
