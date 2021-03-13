package com.example.new_trivia;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    ArrayList<Question> questionArrayList = new ArrayList<>();
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions(final AnswerListAsyncResponse callBack) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i=0;i < response.length();i++){
                            Question question = new Question();
                            try {
                                question.setAnswer(response.getJSONArray(i).get(0).toString());
                                question.setAnswertrue(response.getJSONArray(i).getBoolean(1));

                                questionArrayList.add(question);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(callBack != null) callBack.processfinished(questionArrayList);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "onErrorResponse: "+error);

            }

        });
        Appcontroller.AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionArrayList;

    }
}
