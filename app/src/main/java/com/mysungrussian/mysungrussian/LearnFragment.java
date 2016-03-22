package com.mysungrussian.mysungrussian;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.musicg.wave.Wave;
import com.musicg.wave.WaveHeader;
import com.musicg.wave.extension.Spectrogram;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LearnFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LearnFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LearnFragment extends Fragment {

    private OnFragmentInteractionListener mListener;


    private static final String LOG_TAG = "LearnFragment";

    private static String mFileName = null;
    private static String mWavFileName = null;

    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;

    int BufferElements2Rec = 1024; // will be reassigned later
    int BytesPerElement = 2; // 2 bytes in 16bit format

    Wave wave = null;
    byte[] waveBuffer = null;

    // For plotting the spectrogram
    private LineChart mLineChart;

    private void playRecording() {

        String fileName = mFileName;
        File file = new File(fileName);

        byte[] audioData = null;

        try {
            InputStream inputStream = new FileInputStream(fileName);

            int minBufferSize = AudioTrack.getMinBufferSize(44100,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT);
            audioData = new byte[minBufferSize];

            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,44100,AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,minBufferSize,AudioTrack.MODE_STREAM);
            audioTrack.play();
            int i=0;

            while((i = inputStream.read(audioData)) != -1) {
                audioTrack.write(audioData,0,i);
            }

        } catch(FileNotFoundException fe) {
            Log.e(LOG_TAG,"File not found");
        } catch(IOException io) {
            Log.e(LOG_TAG,"IO Exception");
        }
    }

    private void startRecording() {

        BufferElements2Rec = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING);
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);

        recorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private void writeAudioDataToFile() {
        // Write the output audio in byte

        //String filePath = "/sdcard/voice8K16bitmono.pcm";
        String filePath = mFileName;
        Log.d("Che", "filePath is "+filePath);

        short sData[] = new short[BufferElements2Rec];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            recorder.read(sData, 0, BufferElements2Rec);
            System.out.println("Short wirting to file" + sData.toString());
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, BufferElements2Rec * BytesPerElement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        // stops the recording activity
        if (null != recorder) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            recordingThread = null;
            Log.d("Che","stopRecording");
        }
    }


    // onClick for the play button
    // Play the 1 sec .wav file recorded before...........
    public void onClickPlay (View v){
        final Button btn_play = (Button)getActivity().findViewById(R.id.btn_play);
        btn_play.setClickable(false);
        btn_play.setAlpha(.5f);

        //Start playing
        //onPlay(true);
        playRecording();
        btn_play.setClickable(true);
        btn_play.setAlpha(1.0f);

        //wait 1 second and stop
        btn_play.postDelayed(new Runnable() {

            @Override
            public void run() {
                //btn_play.setClickable(true);
                //btn_play.setAlpha(1.0f);
                //onPlay(false);
            }
        }, 1000);

    }

    // onClick for the Record button
    // Starts recording and stop after 1 sec, save to .wav file to be processed by musicg
    public void onClickRecord (View v){
        final Button btn_record = (Button) getActivity().findViewById(R.id.btn_record);
        btn_record.setClickable(false);
        btn_record.setAlpha(.5f);

        //Start recording
        startRecording();

        //wait 1 second and stop
        btn_record.postDelayed(new Runnable() {

            @Override
            public void run() {
                btn_record.setClickable(true);
                btn_record.setAlpha(1.0f);

                stopRecording();

                // Audio processing?
                WaveHeader waveHeader = new WaveHeader();
                waveHeader.setChannels(1);
                waveHeader.setBitsPerSample(16);
                waveHeader.setSampleRate(RECORDER_SAMPLERATE);

                File file = new File(mFileName);
                int size = (int) file.length();
                byte[] bytes = new byte[size];
                try {
                    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                    buf.read(bytes, 0, bytes.length);
                    buf.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                wave = new Wave(waveHeader, bytes);
                mySpectrogram();
            }
        }, 1000);

    }

    /*public byte[] getFrameBytes(){
        audioRecord.read(buffer, 0, frameByteSize);

        // analyze sound
        int totalAbsValue = 0;
        short sample = 0;
        float averageAbsValue = 0.0f;

        for (int i = 0; i < frameByteSize; i += 2) {
            sample = (short)((buffer[i]) | buffer[i + 1] << 8);
            totalAbsValue += Math.abs(sample);
        }
        averageAbsValue = totalAbsValue / frameByteSize / 2;

        //System.out.println(averageAbsValue);

        // no input
        if (averageAbsValue < 30){
            return null;
        }

        return buffer;
    }*/

    // Audio processing with musicg
    private void mySpectrogram (){


        // Get the wave from the audio file...
        //Wave wave = new Wave(mFileName);
        //byte[] b = wave.getBytes();
        //Log.d("Che", mFileName);
        //Log.d("Che", "the data length is "+b.length);

        // Get spectrogram
        Spectrogram spectrogram = wave.getSpectrogram();

        double [][] data = spectrogram.getNormalizedSpectrogramData();
        Log.d("Che", "The data length is: "+data.length);
        ArrayList<Entry> entries = new ArrayList<Entry>() ;
        for (int i=0; i<data.length; i++){
            float sum = 0;
            for (int j=0; j<data[0].length; j++){
                sum += data[i][j];
            }
            entries.add(new Entry(sum, i));
        }
        //Log.d("Che", "The data rowSum is: "+Arrays.toString(rowSum));
        // Plot this thing, following https://github.com/PhilJay/MPAndroidChart/wiki/Setting-Data
        // in this example, a LineChart is initialized from xml
        mLineChart = (LineChart) getActivity().findViewById(R.id.chart);
        if (mLineChart==null){
            Log.d("Che", "Whats your problem?");
        }

        LineDataSet setData = new LineDataSet(entries, "Cherry~~~~");
        setData.setAxisDependency(YAxis.AxisDependency.LEFT);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setData);
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i<data.length; i++){
            xVals.add(Integer.toString(i));
        }
        LineData lineData = new LineData(xVals, dataSets);
        if (mLineChart==null){
            Log.d("Che", "?!!!!");
        } else if (lineData==null) {
            Log.d("Che", "?!!!!!!");
        }
        mLineChart.setData(lineData);
        mLineChart.invalidate(); // refresh

        // Render the spectrogram
        //SpectrogramView mView = new SpectrogramView(getActivity(), data);
        //LinearLayout mLayout = (LinearLayout) getActivity().findViewById(R.id.linearLayout);
        //LinearLayout.LayoutParams lop = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        //mLayout.addView(mView, 0, lop);

    }


    private static class SpectrogramView extends View {
        private Paint paint = new Paint();
        private Bitmap bmp;

        public SpectrogramView(Context context, double [][] data) {
            super(context);

            if (data != null) {
                paint.setStrokeWidth(1);
                int width = data.length;
                int height = data[0].length;

                int[] arrayCol = new int[width*height];
                int counter = 0;
                for(int i = 0; i < height; i++) {
                    for(int j = 0; j < width; j++) {
                        int value;
                        int color;
                        value = 255 - (int)(data[j][i] * 255);
                        color = (value<<16|value<<8|value|255<<24);
                        arrayCol[counter] = color;
                        counter ++;
                    }
                }
                bmp = Bitmap.createBitmap(arrayCol, width, height, Bitmap.Config.ARGB_8888);
                bmp = Bitmap.createScaledBitmap(bmp, 270, 512, false);
                Log.d("Che", "width "+width+", height "+height);

            } else {
                System.err.println("Data Corrupt");
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(bmp, 0, 100, paint);
        }
    }

    public LearnFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LearnFragment newInstance(String param1, String param2) {
        LearnFragment fragment = new LearnFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.raw";

        /*int recBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfiguration, audioEncoding); // need to be larger than size of a frame
        Log.d("Che", "recBufSize is "+recBufSize);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfiguration, audioEncoding, recBufSize);
        buffer = new byte[frameByteSize];*/


    }

    @Override
    public void onPause() {
        super.onPause();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_learn, container, false);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
