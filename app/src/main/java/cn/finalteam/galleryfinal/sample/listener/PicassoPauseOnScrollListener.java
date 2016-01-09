package cn.finalteam.galleryfinal.sample.listener;

import com.squareup.picasso.Picasso;

import cn.finalteam.galleryfinal.PauseOnScrollListener;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2016/1/9 0009 18:53
 */
public class PicassoPauseOnScrollListener extends PauseOnScrollListener {

    public PicassoPauseOnScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
        super(pauseOnScroll, pauseOnFling);
    }

    @Override
    public void resume() {
        Picasso.with(getActivity()).resumeTag(getActivity());
    }

    @Override
    public void pause() {
        Picasso.with(getActivity()).pauseTag(getActivity());
    }
}
