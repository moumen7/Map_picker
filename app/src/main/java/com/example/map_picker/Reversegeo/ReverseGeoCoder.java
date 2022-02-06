/*
The MIT License (MIT)
[OSI Approved License]
The MIT License (MIT)

Copyright (c) 2014 Daniel Glasson

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.example.map_picker.Reversegeo;


import android.content.res.AssetManager;

import androidx.annotation.Nullable;

import com.example.map_picker.Reversegeo.kdtree.KDTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * Created by Daniel Glasson on 18/05/2014.
 * Uses KD-trees to quickly find the nearest point
 * 
 * ReverseGeoCoder reverseGeoCode = new ReverseGeoCoder(new FileInputStream("c:\\AU.txt"), true);
 * System.out.println("Nearest to -23.456, 123.456 is " + geocode.nearestPlace(-23.456, 123.456));
 */

public class ReverseGeoCoder {

    private static ReverseGeoCoder reverseGeoCoder;

    KDTree<GeoName> kdTree;
    
    // Get placenames from http://download.geonames.org/export/dump/
    /**
     * Parse the zipped geonames file.
     * @param zippedPlacednames a {@link ZipInputStream} zip file downloaded from http://download.geonames.org/export/dump/; can not be null.
     * @param majorOnly only include major cities in KD-tree.
     * 
     * @throws IOException if there is a problem reading the {@link ZipInputStream}.
     * @throws NullPointerException if zippedPlacenames is {@code null}.
     */
    private ReverseGeoCoder(ZipInputStream zippedPlacednames, boolean majorOnly) throws IOException {
        //depending on which zip file is given,
        //country specific zip files have read me files
        //that we should ignore
        ZipEntry entry;
        do{
            entry = zippedPlacednames.getNextEntry();
        }while(entry.getName().equals("readme.txt"));
       
        createKdTree(zippedPlacednames, majorOnly);
        
    }
    /**
     * Parse the raw text geo-names file.
     *
     * @param placenames the text file downloaded from http://download.geonames.org/export/dump/; can not be null.
     * @param majorOnly only include major cities in KD-tree.
     * 
     * @throws IOException if there is a problem reading the stream.
     * @throws NullPointerException if zippedPlacenames is {@code null}.
     */
     public ReverseGeoCoder(InputStream placenames, boolean majorOnly ) throws IOException {
        createKdTree(placenames, majorOnly);
    }

    /**
     * Initialize the reverse geo-coder for the application
     *
     * @param assetManager
     * @param assetPath
     * @param majorOnly
     * @throws IOException
     */
    public static void initialize(AssetManager assetManager,String assetPath, boolean majorOnly) throws IOException{
        ZipInputStream zipInputStream = new ZipInputStream(assetManager.open(assetPath));
        reverseGeoCoder = new ReverseGeoCoder(zipInputStream,majorOnly);
    }

    /**
     * Initialize the reverse geo-coder for the application.
     *
     * @param zippedPlacednames
     * @param majorOnly
     * @throws IOException
     */
    private static void initialize(ZipInputStream zippedPlacednames, boolean majorOnly) throws IOException{
        reverseGeoCoder = new ReverseGeoCoder(zippedPlacednames,majorOnly);
    }

    public static void initialize(InputStream placenames, boolean majorOnly ) throws IOException{
        reverseGeoCoder = new ReverseGeoCoder(placenames,majorOnly);
    }

    /**
     * Gets the instance of the geocoder.
     * <br>
     * This may return null, unless the class has been initialized at the entry point of the application
     * by either {@linkplain #initialize(InputStream, boolean)} or {@linkplain #initialize(ZipInputStream, boolean)}
     *
     * @return
     */
    @Nullable
    public static ReverseGeoCoder getInstance(){
        return reverseGeoCoder;
    }

    private void createKdTree(InputStream placenames, boolean majorOnly)
            throws IOException {
        ArrayList<GeoName> arPlaceNames;
        arPlaceNames = new ArrayList<GeoName>();
        // Read the geonames file in the directory
        BufferedReader in = new BufferedReader(new InputStreamReader(placenames));
        String str;
        try {
            while ((str = in.readLine()) != null) {
                GeoName newPlace = new GeoName(str);
                if ( !majorOnly || newPlace.majorPlace ) {
                    arPlaceNames.add(newPlace);
                }
            }
        } catch (IOException ex) {
            throw ex;
        }finally{
            in.close();
        }
        kdTree = new KDTree<GeoName>(arPlaceNames);
    }

    public GeoName nearestPlace(double latitude, double longitude) {
        return kdTree.findNearest(new GeoName(latitude,longitude));
    }

}
//    The MIT License (MIT)
//        [OSI Approved License]
//        The MIT License (MIT)
//
//        Copyright (c) 2014 Daniel Glasson
//
//        Permission is hereby granted, free of charge, to any person obtaining a copy
//        of this software and associated documentation files (the "Software"), to deal
//        in the Software without restriction, including without limitation the rights
//        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//        copies of the Software, and to permit persons to whom the Software is
//        furnished to do so, subject to the following conditions:
//
//        The above copyright notice and this permission notice shall be included in
//        all copies or substantial portions of the Software.
//
//        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//        THE SOFTWARE.
