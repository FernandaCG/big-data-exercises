package nearsoft.academy.bigdata.recommendation;

import nearsoft.academy.bigdata.recommendation.entity.Review;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MovieRecommender {

    //DataModel model;
    UserSimilarity similarity;


    /*{
        try {
            model = new FileDataModel(new File("/Users/nsf-fcampos/Documents/bigDataProblem/movies.txt.gz"));
            System.out.println(model.getNumItems());
            return model.getNumItems();
            similarity = new PearsonCorrelationSimilarity(model);
        } catch (IOException | TasteException e) {
            e.printStackTrace();
        }

        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

        List recommendations = null;
        try {
            recommendations = recommender.recommend(2, 3);
            System.out.println(recommendations);
        } catch (TasteException e) {
            e.printStackTrace();
        }
    }*/


    public MovieRecommender(String s) {
    }

    public MovieRecommender(){}
    public long getTotalReviews() throws IOException, TasteException {

        BufferedReader in = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream("/Users/nsf-fcampos/Documents/bigDataProblem/movies.txt.gz"))));

        String content;
        int Contador =0;
        while ((content = in.readLine()) != null){
            if(content.contains("product/productId:")){
                Contador++;
            }
        }
        return Contador;
    }

    public long getTotalProducts() throws IOException, TasteException{

        BufferedReader in = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream("/Users/nsf-fcampos/Documents/bigDataProblem/movies.txt.gz"))));
        String content;
        List<String> products =  new ArrayList<>();
        while ((content = in.readLine()) != null){
            if(content.contains("product/productId:")){
                if(!products.contains(content)){
                    products.add(content);
                }
            }
        }
        return products.size();
    }

    public Integer getTotalUsers() throws IOException, TasteException{

        BufferedReader in = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream("/Users/nsf-fcampos/Documents/bigDataProblem/movies.txt.gz"))));

        String content;
        List<String> users =  new ArrayList<>();
        while ((content = in.readLine()) != null){
            if(content.contains("review/userId:")){
                if(!users.contains(content)){
                    users.add(content);
                }
            }
        }
        return users.size();
    }

    public List<String> getRecommendationsForUser(String User){
        List<String> recommendations =  new ArrayList<>();

        return recommendations;
    }
}
