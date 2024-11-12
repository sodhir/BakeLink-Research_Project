package com.example.bakelink.bakers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CalendarView;
//import com.prolificinteractive.materialcalendarview.CalendarDay;
//import com.prolificinteractive.materialcalendarview.DayViewFacade;
//import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.HashSet;
import java.util.Set;

public class B_MyScheduleActivity extends AppCompatActivity {

    private CalendarView calendarView;
    //private MaterialCalendarView calendarView;
    private SwitchCompat blockDaySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bmy_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calendarView = findViewById(R.id.calendarView);
        blockDaySwitch = findViewById(R.id.blockDaySwitch);

        // Handle calendar date selection
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month + 1) + "/" + year;
            Toast.makeText(B_MyScheduleActivity.this, "Selected Date: " + date, Toast.LENGTH_SHORT).show();
            // Add logic to show order details for the selected date
        });

        /*calendarView = findViewById(R.id.calendarView);

        // Sample data for orders (dates and types)
        Set<CalendarDay> regularOrders = new HashSet<>();
        Set<CalendarDay> customOrders = new HashSet<>();

        // Example: Add some regular and custom orders to the set
        regularOrders.add(CalendarDay.from(2024, 11, 16)); // Regular Order on Nov 16
        customOrders.add(CalendarDay.from(2024, 11, 16)); // Custom Order on Nov 16
        customOrders.add(CalendarDay.from(2024, 11, 18)); // Custom Order on Nov 18

        // Decorate the calendar
        calendarView.addDecorator(new EventDecorator(Color.parseColor("#5D9B9B"), regularOrders)); // Regular Order Dots
        calendarView.addDecorator(new EventDecorator(Color.parseColor("#B94553"), customOrders)); // Custom Order Dots*/

        // Set an initial checked state
        blockDaySwitch.setChecked(false); // depending on the default state

        // Add a listener to detect switch state changes
        blockDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Code to execute when switch is turned ON
                    Toast.makeText(getApplicationContext(), "Day blocked", Toast.LENGTH_SHORT).show();
                } else {
                    // Code to execute when switch is turned OFF
                    Toast.makeText(getApplicationContext(), "Day unblocked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        bottomNavigationView.setSelectedItemId(R.id.nav_schedule);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_MyScheduleActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                //no action
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_MyScheduleActivity.this, B_MyCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_MyScheduleActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });

    }

    // Decorator class to add colored dots
    /*public class EventDecorator implements com.prolificinteractive.materialcalendarview.DayViewDecorator {
        private final int color;
        private final Set<CalendarDay> dates;

        public EventDecorator(int color, Set<CalendarDay> dates) {
            this.color = color;
            this.dates = dates;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            // Check if the current day is in the set of dates
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            // Add a dot to the day
            view.addSpan(new DotSpan(10, color));  // Adds a dot with the specified color
        }
    }*/
}