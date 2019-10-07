package nearsoft.academy.bigdata.recommendation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class MovieRecommender {

    private static final String CSV_FILE_PATH = "reviews.csv";
    private long totalUsers=0, totalProducts=0, totalReviews=0;
    public BiMap<String, Long> hashUsers = HashBiMap.create();
    public BiMap<String, Long> hashProducts =  HashBiMap.create();
    private String filePath="", score="", reviewId="", userId="";
    private File newCSV;

    public MovieRecommender(String resourceFile) throws IOException, TasteException {
        this.filePath = resourceFile;
        getTotals(filePath);
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public long getTotalReviews() {
        return totalReviews;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void getTotals(String resourceFile) throws IOException, TasteException{

        BufferedReader in = new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream(resourceFile))));

        newCSV = new File(CSV_FILE_PATH);
        Files.deleteIfExists(newCSV.toPath());
        FileWriter fileWriter = new FileWriter(CSV_FILE_PATH);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        String actualLine;
        while ((actualLine = in.readLine()) != null){
            if(actualLine.contains("review/userId")){
                userId=actualLine.substring(15);
                if(!hashUsers.containsKey(userId)){
                    totalUsers++;
                    hashUsers.put(userId, totalUsers);
                }
            }
            if(actualLine.contains("product/productId:")){
                totalReviews++;
                reviewId=actualLine.substring(19);
                if(!hashProducts.containsKey(reviewId)){
                    hashProducts.put(reviewId, totalProducts);
                    totalProducts++;
                }
            }
            if (actualLine.contains("review/score:")) {
                score = actualLine.substring(14);
                printWriter.printf(hashUsers.get(userId) + "," +hashProducts.get(reviewId)+ "," + score+"\n");
            }
        }
        printWriter.close();
    }

    public List<String> getRecommendationsForUser(String userId) throws IOException, TasteException {
        List<String> recommendationsUser = new ArrayList<>();
        DataModel model = new FileDataModel(newCSV);
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        List<RecommendedItem> recommendations = recommender.recommend(hashUsers.get(userId), 3);
        for (RecommendedItem recommendation : recommendations) {
            recommendationsUser.add(hashProducts.inverse().get(recommendation.getItemID()));
        }
        return recommendationsUser;
    }

}
