package base.engineering.motosumiyoshi.sakeapp.httpclient;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Block;
import com.google.api.services.vision.v1.model.BoundingPoly;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.Page;
import com.google.api.services.vision.v1.model.Paragraph;
import com.google.api.services.vision.v1.model.Symbol;
import com.google.api.services.vision.v1.model.TextAnnotation;
import com.google.api.services.vision.v1.model.TextProperty;
import com.google.api.services.vision.v1.model.Vertex;
import com.google.api.services.vision.v1.model.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import base.engineering.motosumiyoshi.sakeapp.model.LinePoint;
import base.engineering.motosumiyoshi.sakeapp.model.OCRBox;

public class VisionApiWrapper {

    public VisionApiWrapper() {
    }

    public void execOCR(byte[] image, VisionApiCallback callback) {
        try {
            VisionApiAsyncTask task = new VisionApiAsyncTask(image, callback);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface VisionApiCallback {

        void onSuccess(List<OCRBox> result);

        void onFail();
    }


    private class VisionApiAsyncTask extends AsyncTask<Void, Void, List<OCRBox>> {

        private Vision vision;

        private BatchAnnotateImagesRequest batchRequest;

        private VisionApiCallback callback;

        public VisionApiAsyncTask(byte[] imageBytes, VisionApiCallback callback) {
            this.callback = callback;
            this.vision = new Vision.Builder(
                    new NetHttpTransport(),
                    new AndroidJsonFactory(), null)
                    .setVisionRequestInitializer(new VisionRequestInitializer(getAPIKey()))
                    .build();
            Image image = new Image();
            image.encodeContent(imageBytes);

            Feature desiredFeature = new Feature();
            desiredFeature.setType("TEXT_DETECTION");

            AnnotateImageRequest request = new AnnotateImageRequest();
            request.setImage(image);
            request.setFeatures(Arrays.asList(desiredFeature));
            this.batchRequest = new BatchAnnotateImagesRequest();
            batchRequest.setRequests(Arrays.asList(request));
        }

        @Override
        protected List<OCRBox> doInBackground(Void... params) {
            List<OCRBox> retVal = new ArrayList<>();
            try {
                BatchAnnotateImagesResponse batchResponse =
                        vision.images().annotate(batchRequest).execute();
                TextAnnotation annotation =
                        batchResponse.getResponses().get(0).getFullTextAnnotation();
                if (annotation != null) {
                    List<Page> pages = annotation.getPages();
                    for (Page page : pages) {
                        for (Block block : page.getBlocks()) {
                            for (Paragraph paragraph : block.getParagraphs()) {
                                // テキスト
                                StringBuilder textBuilder = new StringBuilder();
                                for (Word word : paragraph.getWords()) {
                                    for (Symbol symbol : word.getSymbols()) {
                                        String text = symbol.getText();
                                        textBuilder.append(text);
                                    }
                                }
                                // 頂点
                                List<LinePoint> points = new ArrayList<>();
                                for (Vertex vertex : paragraph.getBoundingBox().getVertices()) {
                                    int x = vertex.getX();
                                    int y = vertex.getY();
                                    points.add(new LinePoint(x, y));
                                }
                                retVal.add(new OCRBox(textBuilder.toString(), points));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return retVal;
        }

        @Override
        protected void onPostExecute(List<OCRBox> result) {
            if (result == null || result.size() == 0) {
                callback.onFail();
            } else {
                callback.onSuccess(result);
            }
        }
    }

    private String getAPIKey() {
        return (new Object() {
            int t;
            public String toString() {
                byte[] buf = new byte[39];
                t = 1829507334;
                buf[0] = (byte) (t >>> 2);
                t = 653551929;
                buf[1] = (byte) (t >>> 5);
                t = -679835620;
                buf[2] = (byte) (t >>> 16);
                t = -888407921;
                buf[3] = (byte) (t >>> 19);
                t = 1945804630;
                buf[4] = (byte) (t >>> 11);
                t = 444164398;
                buf[5] = (byte) (t >>> 16);
                t = 520250422;
                buf[6] = (byte) (t >>> 4);
                t = -564481485;
                buf[7] = (byte) (t >>> 22);
                t = 261919779;
                buf[8] = (byte) (t >>> 9);
                t = -1624629766;
                buf[9] = (byte) (t >>> 6);
                t = 619563196;
                buf[10] = (byte) (t >>> 17);
                t = 1705918908;
                buf[11] = (byte) (t >>> 18);
                t = -1852616488;
                buf[12] = (byte) (t >>> 6);
                t = -908785945;
                buf[13] = (byte) (t >>> 4);
                t = 1294441918;
                buf[14] = (byte) (t >>> 6);
                t = 360016725;
                buf[15] = (byte) (t >>> 12);
                t = -554218025;
                buf[16] = (byte) (t >>> 5);
                t = -1353003634;
                buf[17] = (byte) (t >>> 2);
                t = -62531342;
                buf[18] = (byte) (t >>> 10);
                t = 678731696;
                buf[19] = (byte) (t >>> 16);
                t = -1831672025;
                buf[20] = (byte) (t >>> 20);
                t = 31083017;
                buf[21] = (byte) (t >>> 11);
                t = -1595073091;
                buf[22] = (byte) (t >>> 17);
                t = -432419773;
                buf[23] = (byte) (t >>> 6);
                t = -981521616;
                buf[24] = (byte) (t >>> 20);
                t = -900263064;
                buf[25] = (byte) (t >>> 19);
                t = -1463574162;
                buf[26] = (byte) (t >>> 17);
                t = 1677374726;
                buf[27] = (byte) (t >>> 7);
                t = 770262786;
                buf[28] = (byte) (t >>> 13);
                t = 915665261;
                buf[29] = (byte) (t >>> 17);
                t = 995697396;
                buf[30] = (byte) (t >>> 7);
                t = -1616795274;
                buf[31] = (byte) (t >>> 7);
                t = 2129076206;
                buf[32] = (byte) (t >>> 12);
                t = -1239660536;
                buf[33] = (byte) (t >>> 20);
                t = 1686866210;
                buf[34] = (byte) (t >>> 20);
                t = -1739211594;
                buf[35] = (byte) (t >>> 2);
                t = -1169807509;
                buf[36] = (byte) (t >>> 23);
                t = 1194679787;
                buf[37] = (byte) (t >>> 24);
                t = 1059815810;
                buf[38] = (byte) (t >>> 3);
                return new String(buf);
            }
        }.toString());
    }
}
