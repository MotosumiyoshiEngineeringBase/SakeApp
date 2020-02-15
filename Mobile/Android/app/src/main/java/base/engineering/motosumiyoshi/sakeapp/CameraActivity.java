package base.engineering.motosumiyoshi.sakeapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    // 描画プレビューエリア
    private TextureView cameraView;

    // ローテーション定義
    private int orientation;

    // ローテーション定義
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    // カメラID
    private String cameraId;

    // カメラデバイス
    private CameraDevice cameraDevice;

    // カメラキャプチャセッション
    private CameraCaptureSession cameraCaptureSession;

    // カメラキャプチャリクエストビルダー
    private CaptureRequest.Builder captureRequestBuilder;

    // 画像サイズ
    private Size imageDimension;

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private Handler mBackgroundHandler;

    private HandlerThread mBackgroundThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // 描画エリアの初期化
        this.cameraView = findViewById(R.id.image_area);
        this.cameraView.setSurfaceTextureListener(textureListener);

        // 撮影ボタン押下時、撮影実行
        ImageButton captureButton = findViewById(R.id.btn_capture);
        captureButton.setOnClickListener(v -> {
            capture();
        });

        // ホームに戻るボタン
        ImageButton homeButton = findViewById(R.id.btn_return_home);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    /**
     * 描画エリアの初期化完了イベントリスナー
     */
    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            // 描画エリアの初期化が完了したらカメラをオープン
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    /**
     * カメラをオープン
     */
    private void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CAMERA_PERMISSION);
                return;
            }
            // カメラマネージャーを取得し、背面カメラを取得
            CameraManager manager =
                    (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            this.cameraId = getCameraId(manager);

            // プレビューのサイズを取得
            CameraCharacteristics characteristics =
                    manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map =
                    characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            this.imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            // カメラをオープン
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * カメラマネージャから背面カメラを取得する
     */
    private String getCameraId(CameraManager manager) throws CameraAccessException {
        String[] cameraIdList = manager.getCameraIdList();
        String mCameraId = null;
        for (String cameraId : cameraIdList) {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            switch (characteristics.get(CameraCharacteristics.LENS_FACING)) {
                case CameraCharacteristics.LENS_FACING_FRONT:
                    break;
                case CameraCharacteristics.LENS_FACING_BACK:
                    mCameraId = cameraId;
                    break;
                default:
            }
        }
        return mCameraId;
    }

    /**
     * カメラオープン終了のイベントのリスナー
     */
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            // カメラのオープンが正常終了した場合、カメラキャプチャセッションを作成
            cameraDevice = camera;
            createCameraCaptureSession();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            if (cameraDevice != null) {
                cameraDevice.close();
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            if (cameraDevice != null) {
                cameraDevice.close();
            }
            cameraDevice = null;
        }
    };

    /**
     * カメラキャプチャセッションの作成
     */
    private void createCameraCaptureSession() {
        try {
            SurfaceTexture texture = cameraView.getSurfaceTexture();
            texture.setDefaultBufferSize(
                    imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(
                    Arrays.asList(surface),
                    captureSessionStateCallback,
                   null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * カメラキャプチャセッションの作成完了リスナー
     */
     private CameraCaptureSession.StateCallback captureSessionStateCallback =
             new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            // カメラキャプチャセッションの作成が完了した場合
            cameraCaptureSession = session;
            // 3Aモードを有効化
            captureRequestBuilder.set(
                    CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            try {
                // カメラプレビューを開始
                session.setRepeatingRequest(
                        captureRequestBuilder.build(),
                        null,
                        mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(
                @NonNull CameraCaptureSession cameraCaptureSession) {
            Toast.makeText(
                    CameraActivity.this,
                    "Configuration failed",
                    Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 撮影処理
     */
    private void capture() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics =
                    manager.getCameraCharacteristics(cameraDevice.getId());
            int width = 84;
            int height = 114;
            ImageReader reader =
                    ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(cameraView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            // 回転
            int sensorOrientation = 0;
            Integer value = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            if (value != null) {
                sensorOrientation = value;
            }
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            this.orientation = (ORIENTATIONS.get(rotation) + sensorOrientation + 270) % 360;
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, orientation);

            // 画像取得
            reader.setOnImageAvailableListener(imageReader -> {
                Image image = null;
                try {
                    image = imageReader.acquireLatestImage();
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] imageBinary = new byte[buffer.capacity()];
                    buffer.get(imageBinary);
                    // 画面遷移
                    Intent intent = new Intent(
                            getApplicationContext(), ProductActivity.class);
                    intent.putExtra("CAPTURED_IMAGE", imageBinary);
                    intent.putExtra("ORIENTATION", orientation);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (image != null) {
                        image.close();
                    }
                }
            }, mBackgroundHandler);

            cameraDevice.createCaptureSession(
                    outputSurfaces,
                    new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(
                                captureBuilder.build(),
                                captureListener,
                                mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 撮影完了処理のイベントリスナー
     */
    private final CameraCaptureSession.CaptureCallback captureListener =
            new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(
                        CameraCaptureSession session,
                        CaptureRequest request,
                        TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    createCameraCaptureSession();
                }
            };

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(
                        CameraActivity.this,
                        "Can't use this app without granting permission",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if (cameraView.isAvailable()) {
            openCamera();
        } else {
            cameraView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {
        stopBackgroundThread();
        super.onPause();
    }
}