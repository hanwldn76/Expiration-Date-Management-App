package com.example.project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.*;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MyCalendar extends AppCompatActivity {

    MaterialCalendarView materialCalendarView;
    TextView textView;
    Button updateButton;

    SQLiteDatabase sqlDB;
    myDBHelper myHelper;

    // 기존 Decorator를 저장할 리스트
    List<DayViewDecorator> originalDecorators = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        setTitle("유통기한 관리 어플");

        updateButton = (Button) findViewById(R.id.updateButton);
        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        textView = (TextView) findViewById(R.id.textView1);
        myHelper = new myDBHelper(this);

        // 기존 Decorator를 저장
        originalDecorators.add(new SundayDecorator());
        originalDecorators.add(new SaturdayDecorator());
        originalDecorators.add(new MySelectorDecorator(this));

        materialCalendarView.addDecorators(originalDecorators.toArray(new DayViewDecorator[0]));

        // 날짜 선택 이벤트 처리
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // 선택된 날짜에 대한 처리
                handleSelectedDate(date);
            }
        });

        // 두 번째 액티비티에서 첫 번째 액티비티의 데이터를 업데이트하는 버튼 예시
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 두 번째 액티비티에서 첫 번째 액티비티의 DB 값을 다시 불러옴
                loadDatabaseData();
            }
        });
    }

    private void handleSelectedDate(CalendarDay date) {
        // 선택된 날짜에 대한 처리
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectedDateStr = dateFormat.format(date.getDate());

        // 해당 날짜에 대한 정보를 DB에서 가져와서 TextView에 표시
        String info = getInfoForSelectedDate(selectedDateStr);
        textView.setText(info);
    }

    private String getInfoForSelectedDate(String selectedDate) {
        // DB에서 해당 날짜에 대한 정보를 가져와서 반환하는 로직을 작성
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT iname, idate FROM infoTBL WHERE idate = ?", new String[]{selectedDate});

        StringBuilder infoStringBuilder = new StringBuilder();
        while (cursor.moveToNext()) {
            // 여기에서 필요한 정보를 가져와서 StringBuilder에 추가
            String name = cursor.getString(0);
            String date = cursor.getString(1);
            infoStringBuilder.append("재료명: ").append(name).append("\n유통기한: ").append(date).append("\n\n");
        }

        cursor.close();
        sqlDB.close();

        return infoStringBuilder.toString();
    }

    // 두 번째 액티비티에서 첫 번째 액티비티의 DB 값을 불러오는 메서드
    private void loadDatabaseData() {
        sqlDB = myHelper.getWritableDatabase();

        // 기존에 표시된 날짜 초기화
        materialCalendarView.removeDecorators();

        // 기존 Decorator 추가
        materialCalendarView.addDecorators(originalDecorators.toArray(new DayViewDecorator[0]));


        // 기존 로직: infoTBL에서 데이터를 가져와서 표시를 추가
        Cursor cursor = sqlDB.rawQuery("SELECT idate FROM infoTBL", null);
        while (cursor.moveToNext()) {
            String dateString = cursor.getString(0);

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = dateFormat.parse(dateString);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                CalendarDay calendarDay = CalendarDay.from(calendar);

                // 캘린더뷰에 날짜 표시
                materialCalendarView.addDecorator(new EventDecorator(Color.RED, Collections.singleton(calendarDay)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cursor.close();
        sqlDB.close();
    }
}
