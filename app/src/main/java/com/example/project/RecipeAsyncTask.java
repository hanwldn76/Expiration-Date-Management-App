package com.example.project;

import android.os.AsyncTask;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

// AsyncTask를 확장하는 클래스 정의
public class RecipeAsyncTask extends AsyncTask<String, Void, String[]> {
    private TextView[] recipe;  // AsyncTask 내에서 사용할 배열

    // 배열 설정 메서드
    public void setRecipe(TextView[] recipe) {
        this.recipe = recipe;
    }
    // 백그라운드 처리를 수행하는 doInBackground 메서드 오버라이드
    @Override
    protected String[] doInBackground(String... params) {
        String url = params[0];
        String[] titles = new String[5];

        try {
            Document doc = Jsoup.connect(url).get();
            Elements recipeItems = doc.select(".common_sp_list_li");

            for (int i = 0; i < 5; i++) {
                Element recipeItem = recipeItems.get(i);

                // 제목 추출
                String title = recipeItem.selectFirst(".common_sp_caption_tit").text();
                titles[i] = title;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return titles;
    }

    // onPostExecute 메서드 오버라이드하여 백그라운드에서 얻은 데이터로 UI 업데이트
    @Override
    protected void onPostExecute(String[] titles) {
        // 백그라운드에서 얻은 데이터로 UI 업데이트하는 메서드 호출
        updateUI(titles);
    }

    // 스크래핑된 데이터로 UI를 업데이트하는 메서드 정의
    private void updateUI(String[] titles) {
        // 스크래핑된 데이터로 UI 업데이트
        for (int i = 0; i < titles.length; i++) {
            recipe[i].setText(titles[i]);
        }
    }
}