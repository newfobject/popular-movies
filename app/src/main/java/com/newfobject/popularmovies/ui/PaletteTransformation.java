package com.newfobject.popularmovies.ui;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public final class PaletteTransformation implements Transformation {
    private static final PaletteTransformation INSTANCE = new PaletteTransformation();
    private static final Map<Bitmap, Palette> CACHE = new WeakHashMap<Bitmap, Palette>();

    /**
     * A {@link Target} that receives {@link Palette} information in its callback.
     * @see Target
     */
    public static abstract class PaletteTarget implements Target {
        /**
         * Callback when an image has been successfully loaded.
         * Note: You must not recycle the bitmap.
         * @param palette The extracted {@linkplain Palette}
         */
        protected abstract void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from, Palette palette);

        @Override public final void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            final Palette palette = getPalette(bitmap);
            onBitmapLoaded(bitmap, from, palette);
        }
    }

    public static Palette getPalette(Bitmap bitmap) {
        return CACHE.get(bitmap);
    }

    /**
     * A {@link Callback} that receives {@link Palette} information in its callback.
     * @see Callback
     */
    public static abstract class PaletteCallback implements Callback {
        private WeakReference<ImageView> mImageView;

        public PaletteCallback(@NonNull ImageView imageView) {
            mImageView = new WeakReference<ImageView>(imageView);
        }

        protected abstract void onSuccess(int backgroundColor, int textColor);

        @Override public final void onSuccess() {
            if (getImageView() == null) {
                return;
            }
            final Bitmap bitmap = ((BitmapDrawable) getImageView().getDrawable()).getBitmap(); // Ew!
            final Palette palette = getPalette(bitmap);
            int colors[] = colorSelector(palette);
            onSuccess(colors[0], colors[1]);
        }

        /**
         * @param palette
         * @return int[Background Color, Text Color]
         */
        private int[] colorSelector(Palette palette) {
            int colors[] = new int[2];
            // first value is background color, second is text color
            Palette.Swatch vibrant = palette.getVibrantSwatch();
            if (vibrant != null) {
                colors[0] = vibrant.getRgb();
                colors[1] = vibrant.getTitleTextColor();
            } else {
                Palette.Swatch vibrantLight = palette.getLightVibrantSwatch();
                if (vibrantLight != null) {
                    colors[0] = vibrantLight.getRgb();
                    colors[1] = vibrantLight.getTitleTextColor();
                } else {
                    Palette.Swatch mutedLight = palette.getLightMutedSwatch();
                    if (mutedLight != null) {
                        colors[0] = mutedLight.getRgb();
                        colors[1] = mutedLight.getTitleTextColor();
                    } else {
                        colors[0] = -14575885;
                        colors[1] = Color.WHITE;
                    }
                }
            }
            return colors;
        }

        private ImageView getImageView() {
            return mImageView.get();
        }
    }

    /**
     * Obtains a {@link PaletteTransformation} to extract {@link Palette} information.
     * @return A {@link PaletteTransformation}
     */
    public static PaletteTransformation instance() {
        return INSTANCE;
    }

    //# Transformation Contract
    @Override public final Bitmap transform(Bitmap source) {
        final Palette palette = Palette.generate(source);
        CACHE.put(source, palette);
        return source;
    }

    @Override public String key() {
        return ""; // Stable key for all requests. An unfortunate requirement.
    }

    private PaletteTransformation() { }
}