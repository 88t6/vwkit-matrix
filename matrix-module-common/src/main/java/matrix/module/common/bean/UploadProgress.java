package matrix.module.common.bean;

import org.apache.commons.fileupload.ProgressListener;

/**
 * @author wangcheng
 * @param <T> 参数
 */
public abstract class UploadProgress<T> implements ProgressListener {

    private T container;

    private Integer uploadRate = 0;

    private long bytesRead = 0L;

    private long bytesReadKBSpeedWith1s = 0L;

    public UploadProgress(T container) {
        this.container = container;
        (new Thread(new Runnable() {
            @Override
            public void run() {
                while (uploadRate < 100) {
                    try {
                        long beforeRead = bytesRead;
                        Thread.sleep(1000);
                        bytesReadKBSpeedWith1s = (bytesRead - beforeRead) / 1024;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        })).start();
    }

    @Override
    public void update(long bytesRead, long contentLength, int items) {
        Integer uploadRate = Math.round((bytesRead * 100 / contentLength));
        this.bytesRead = bytesRead;
        this.uploadRate = uploadRate;
        this.execute(this.container, uploadRate, this.bytesReadKBSpeedWith1s);
    }

    /**
     * @param container 参数
     * @param uploadRate 参数
     * @param bytesReadKBSpeedWith1s 参数
     */
    public abstract void execute(T container, Integer uploadRate, long bytesReadKBSpeedWith1s);

}
