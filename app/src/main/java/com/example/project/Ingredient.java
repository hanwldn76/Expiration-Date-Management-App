package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Ingredient extends AppCompatActivity {
    Button addBtn, selBtn, initBtn;
    View dialogView;
    EditText edtName;
    DatePicker datePicker;
    String year="", month="", day="", name="", date="";
    RadioGroup rdoGroup;
    RadioButton rdoBtn1, rdoBtn2, rdoBtn3, rdoBtn4, rdoBtn5;

    SQLiteDatabase sqlDB;
    myDBHelper myHelper;
    ArrayList<IngredientData> items;

    int num;

    int ingredientImgs[] = {R.drawable.a,R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        setTitle("유통기한 관리 어플");

        items = new ArrayList<IngredientData>();

        ListView listView = (ListView) findViewById(R.id.listView);
        final MyAdapter adapter = new MyAdapter(this, android.R.layout.simple_list_item_1, items);

        //listView.setAdapter(adapter);

        addBtn = (Button) findViewById(R.id.addBtn);
        selBtn = (Button) findViewById(R.id.selBtn);
        initBtn = (Button) findViewById(R.id.initBtn);

        myHelper = new myDBHelper(this);

        initBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();
                myHelper.onUpgrade(sqlDB, 1, 2);
                sqlDB.close();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView = (View) View.inflate(Ingredient.this, R.layout.ingredient_add_dialog, null);

                AlertDialog.Builder dlg = new AlertDialog.Builder(Ingredient.this);
                dlg.setTitle("재료 정보 입력");
                dlg.setView(dialogView);

                dlg.setPositiveButton(
                        "등록",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                edtName = (EditText) dialogView.findViewById(R.id.edtName);
                                name = edtName.getText().toString();

                                if (!isNameAlreadyExists(name)) {

                                    datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
                                    year = Integer.toString(datePicker.getYear());
                                    month = Integer.toString(datePicker.getMonth() + 1);
                                    day = Integer.toString(datePicker.getDayOfMonth());

                                    rdoGroup = (RadioGroup) dialogView.findViewById(R.id.rdoGroup);
                                    rdoBtn1 = (RadioButton) dialogView.findViewById(R.id.rdoBtn1);
                                    rdoBtn2 = (RadioButton) dialogView.findViewById(R.id.rdoBtn2);
                                    rdoBtn3 = (RadioButton) dialogView.findViewById(R.id.rdoBtn3);
                                    rdoBtn4 = (RadioButton) dialogView.findViewById(R.id.rdoBtn4);
                                    rdoBtn5 = (RadioButton) dialogView.findViewById(R.id.rdoBtn5);

                                    switch (rdoGroup.getCheckedRadioButtonId()) {
                                        case R.id.rdoBtn1:
                                            num = 0;
                                            break;
                                        case R.id.rdoBtn2:
                                            num = 1;
                                            break;
                                        case R.id.rdoBtn3:
                                            num = 2;
                                            break;
                                        case R.id.rdoBtn4:
                                            num = 3;
                                            break;
                                        case R.id.rdoBtn5:
                                            num = 4;
                                            break;
                                        default:
                                            Toast.makeText(Ingredient.this, "종류를 선택하세요!", Toast.LENGTH_SHORT).show();
                                    }
                                    date = year + "-" + (month = month.length() == 1 ? "0" + month : month) + "-" + day;

                                    if (adapter.getCount() == 0) {
                                        adapter.addItem(new IngredientData(num, name, date));
                                        listView.setAdapter(adapter);
                                    }

                                    sqlDB = myHelper.getWritableDatabase();
                                    sqlDB.execSQL("INSERT INTO infoTBL VALUES ('" + name + "', '" + date + "', '" + num + "');");
                                    sqlDB.close();

                                    adapter.addItem(new IngredientData(ingredientImgs[num], name, date));
                                    listView.setAdapter(adapter);

                                    Toast.makeText(Ingredient.this, "등록했습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Ingredient.this, "이미 등록된 이름입니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

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

                adapter.notifyDataSetChanged();

            }

        });

        selBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 기존의 데이터를 모두 지우고 새로운 데이터를 추가할 리스트 생성
                ArrayList<IngredientData> newItems = new ArrayList<>();

                sqlDB = myHelper.getWritableDatabase();

                //Cursor라는 그릇에 목록을 담아주기
                Cursor cursor = sqlDB.rawQuery("SELECT * FROM infoTBL", null);

                //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
                while (cursor.moveToNext()) {
                    //num 행은 가장 첫번째에 있으니 0번이 되고, name은 1번
                    newItems.add(new IngredientData(ingredientImgs[cursor.getInt(2)], cursor.getString(0), cursor.getString(1)));
                }

                //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
                adapter.setItems(newItems);
                listView.setAdapter(adapter);

                cursor.close();
                sqlDB.close();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Ingredient.this);
                builder.setTitle("삭제 확인");
                builder.setMessage("선택한 아이템을 삭제하시겠습니까?");

                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 데이터베이스에서도 삭제
                        deleteItemFromDatabase(position);

                        // 리스트뷰에서 해당 아이템 제거
                        items.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();

                return true;
            }
        });
    }

    private void updateIngredient(String originalName, String updatedName, int updatedCategory, String updatedDate) {
        SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("iName", updatedName);
        values.put("iCategory", updatedCategory);
        values.put("iDate", updatedDate);

        sqlDB.update("infoTBL", values, "iName=?", new String[]{originalName});
        sqlDB.close();
    }

    // 데이터베이스에서 아이템 삭제하는 메서드
    private void deleteItemFromDatabase(int position) {
        SQLiteDatabase sqlDB = myHelper.getWritableDatabase();

        // position 값으로 아이템을 가져옴
        String itemNameToDelete = items.get(position).getName();

        // 데이터베이스에서 해당 아이템 삭제
        sqlDB.execSQL("DELETE FROM infoTBL WHERE iName = '" + itemNameToDelete + "';");

        sqlDB.close();
        Toast.makeText(getApplicationContext(), "아이템이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }

    // 중복된 이름이 있는지 확인하는 메서드
    private boolean isNameAlreadyExists(String name) {
        for (IngredientData item : items) {
            if (item.getName().equals(name)) {
                return true; // 중복된 이름이 있다면 true 반환
            }
        }
        return false; // 중복된 이름이 없다면 false 반환
    }
}