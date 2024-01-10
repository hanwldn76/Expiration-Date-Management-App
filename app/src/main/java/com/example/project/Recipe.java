package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Recipe extends AppCompatActivity {
    View dialogView;
    ListView listView;
    Button updateButton;

    TextView [] recipe = new TextView[5];

    SQLiteDatabase sqlDB;
    myDBHelper myHelper;
    ArrayAdapter<String> adapter; // 문자열을 담을 어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        setTitle("유통기한 관리 어플");

        myHelper = new myDBHelper(this);
        listView = findViewById(R.id.listView);
        updateButton = findViewById(R.id.updateButton);

        // 아이템을 담을 어댑터 초기화
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        // 리스트뷰 아이템 클릭 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemName = adapter.getItem(position);

                // 아이템 이름을 기반으로 URL 생성
                String url = "https://www.10000recipe.com/recipe/list.html?q=" + itemName;

                // 웹 브라우저에서 해당 URL 열기
                dialogView = (View) View.inflate(Recipe.this, R.layout.recipe_dialog, null);

                AlertDialog.Builder dlg = new AlertDialog.Builder(Recipe.this);
                dlg.setTitle("레시피 추천");
                dlg.setView(dialogView);

                recipe[0] = (TextView) dialogView.findViewById(R.id.recipe1);
                recipe[1] = (TextView) dialogView.findViewById(R.id.recipe2);
                recipe[2] = (TextView) dialogView.findViewById(R.id.recipe3);
                recipe[3] = (TextView) dialogView.findViewById(R.id.recipe4);
                recipe[4] = (TextView) dialogView.findViewById(R.id.recipe5);

                recipe[0].setBackgroundColor(Color.GRAY);
                recipe[2].setBackgroundColor(Color.GRAY);
                recipe[4].setBackgroundColor(Color.GRAY);

                RecipeAsyncTask task = new RecipeAsyncTask();
                task.setRecipe(recipe);
                task.execute("https://www.10000recipe.com/recipe/list.html?q=" + itemName);

                dlg.setPositiveButton(
                        "이동",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                            }
                        }
                );


                dlg.setNegativeButton(
                        "취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // "취소" 버튼을 눌렀을 때의 처리
                                Toast.makeText(getApplicationContext(), "취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                dlg.show();

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DB에서 데이터 가져와서 리스트뷰에 업데이트
                updateListView();
            }
        });
    }

    private void updateListView() {
        sqlDB = myHelper.getWritableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT iname FROM infoTBL", null);

        if (cursor != null) {
            // 리스트뷰 갱신
            adapter.clear(); // 기존 아이템 삭제

            while (cursor.moveToNext()) {
                String itemName = cursor.getString(0);
                adapter.add(itemName); // 아이템 추가
            }

            // 리스트뷰 갱신 후 커서를 닫음
            cursor.close();
        } else {
            Toast.makeText(this, "No data found.", Toast.LENGTH_SHORT).show();
        }

        sqlDB.close();
    }

    private void openRecipeLink(String recipeLink) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipeLink));
        startActivity(intent);
    }
}
