import java.io.BufferedReader;  
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;   
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator; 

public class PageRank{
    //page rank runtime = O(n * numEpochs + nlogn) 
    public static void main(String[] args){
        //All of the video data will be stored in this arraylist
        ArrayList<Video> videos = new ArrayList<>();
        String dataFileName = "youtube_data.csv";
        try{
            BufferedReader br = new BufferedReader(new FileReader(dataFileName));
            String line = "";  
            String[] input = {};
            br.readLine(); //skip header
            while((line = br.readLine()) != null){
                input = line.split(",");
                videos.add(new Video(input));
            }
            br.close();
        }catch(Exception e ){
            e.printStackTrace();
        }

        //all videos from CSV in ArrayList.
        //set videoRank of each video
        for(Video v : videos){
            v.videoRank = 1.0 / videos.size();
        }

        //Basic PageRank Update Rule
        int pageRankCycles = 1000;
        for(int i = 0; i < pageRankCycles; i++){
            HashMap<String, Double> updates = new HashMap<>();
            for(Video v : videos){
                double contribution = v.videoRank / v.relatedID.size();
                for(String s : v.relatedID){
                    if(updates.containsKey(s)){
                        updates.replace(s, updates.get(s) + contribution);
                    }else{ 
                        updates.put(s, contribution);
                    }
                }
            }
            //double dampeningFactor = 0.9;
            // perform the updates
            for(Video v : videos){
                v.videoRank =  updates.getOrDefault(v.videoID, 0.0);
            }
        }

        //find the most k most relevant
        Collections.sort(videos, new Comparator<Video>() {
            @Override
            public int compare(Video a, Video b){
                return -1 * Double.compare(a.videoRank, b.videoRank);
            }
        });

        int k = 1000;
        //order, length, views, age, rate, ratings, comments, num RelatedIDs
        double[][] topKInformation = new double[k][8];
        for(int i = 0; i < k && i < videos.size(); i++){
            topKInformation[i][0] = i;
            topKInformation[i][1] = videos.get(i).length;
            topKInformation[i][2] = videos.get(i).views;
            topKInformation[i][3] = videos.get(i).age;
            topKInformation[i][4] = videos.get(i).rate;
            topKInformation[i][5] = videos.get(i).ratings;
            topKInformation[i][6] = videos.get(i).comments;
            topKInformation[i][7] = videos.get(i).relatedID.size();
        }
        try{
            writeCSV(topKInformation, "topData.csv");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void writeCSV(double[][] data, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);

        // Write header if present
        if (data.length > 0 && data[0].length > 0) {
            for (int i = 0; i < data[0].length; i++) {
                writer.write(String.valueOf(data[0][i]));
                if (i < data[0].length - 1) {
                    writer.write(",");
                }
            }
            writer.write("\n");
        }

        // Write data lines
        for (int i = 1; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                writer.write(String.valueOf(data[i][j]));
                if (j < data[i].length - 1) {
                    writer.write(",");
                }
            }
            writer.write("\n");
        }

        writer.close();
    }


    private static class Video{
        private double length, views, age, ratings, comments, videoRank;
        private String videoID, uploader, category;
        private float rate;
        private ArrayList<String> relatedID;

        private Video(String[] inputLine){
            this.videoID = inputLine[0];
            this.uploader = inputLine[1];
            this.age = Double.parseDouble(inputLine[2]);
            this.category = inputLine[3];
            this.length = Double.parseDouble(inputLine[4]);
            this.views = Double.parseDouble(inputLine[5]);
            this.rate = Float.parseFloat(inputLine[6]);
            this.ratings = Double.parseDouble(inputLine[7]); 
            this.comments = Double.parseDouble(inputLine[8]);
            //add the rest of the columns into the relatedIDs field. 
            //could be a variable number of relatedIDs from 0-20
            relatedID = new ArrayList<>();
            for(int i = 9; i < inputLine.length; i++){
                this.relatedID.add(inputLine[i]);
            }
        }


    }

}