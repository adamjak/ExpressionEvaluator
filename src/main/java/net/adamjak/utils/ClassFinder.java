/*
 * Copyright (c) 2016, Marian Adamjak
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.adamjak.utils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author Marian Adamjak
 */
public class ClassFinder {
    
    private static final String DOT =".";
    private static final String SLASH = "/";
    private static final String FILE_PROTOCOL = "file";
    private static final String CLASS_SUFFIX = ".class";
    
    
    private final ClassLoader classLoader;

    public ClassFinder(){
        this.classLoader = Thread.currentThread().getContextClassLoader();
    }
    
    public ClassFinder(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    public List<Class<?>> findAnnotatedClass(String packageName, boolean includeSubpackage, Class<? extends Annotation>... annotationClasses){
        List<Class<?>> classes = findClasses(packageName, includeSubpackage);
        List<Class<?>> outputList = new ArrayList<>();
        if(Utils.isCollectionNullOrEmpty(classes) || (annotationClasses == null || annotationClasses.length == 0)){
            return outputList;
        }
        for(Class<?> cls : classes){
            for(Class<? extends Annotation> annoCls : annotationClasses){
                if(cls.getAnnotationsByType(annoCls).length > 0){
                    outputList.add(cls);
                    break;
                }
            }
        }
        return outputList;
    }
    
    public List<Class<?>> findClasses(String packageName, boolean includeSubpackage){
        String sourceName = rewritePackageName(packageName);
        List<String> clsNameList = new ArrayList<>();
        Enumeration<URL> resources;
        try {
            resources = this.classLoader.getResources(sourceName);
        } catch (IOException ex) {
            return new ArrayList<>();
        }
        while (resources.hasMoreElements()) {
            URL rscUrl = resources.nextElement();
            if(FILE_PROTOCOL.equals(rscUrl.getProtocol())){
                File file = new File(rscUrl.getFile());
                clsNameList.addAll(findClassFileNames(file));
            }
        }
        List<Class<?>> outputList = new ArrayList<>();
        String packageSearchPattern = packageName.replace(DOT,File.separator);
        int maxDots = Utils.countCharInString(packageName, '.')+1;
        for(String classFileName : clsNameList){            
            int startIndex = classFileName.lastIndexOf(packageSearchPattern);
            if(startIndex >= 0){
                final int endIndex = classFileName.length() - CLASS_SUFFIX.length();
                String className = classFileName.substring(startIndex,endIndex).replace(File.separator, DOT);
                if(includeSubpackage || maxDots>= Utils.countCharInString(className, '.')){
                    Class<?> cls;
                    try {
                        cls = this.classLoader.loadClass(className);
                        outputList.add(cls);
                    } catch (ClassNotFoundException ex) {
                    }
                }                
            }
        }
        return outputList;
    }
    
    private List<String> findClassFileNames(File file){
        List<String> outputList = new ArrayList<>();
        if(file == null){
            return outputList;
        }
        if(file.isDirectory()){
            for(File inFile : file.listFiles()){
                outputList.addAll(findClassFileNames(inFile));
            }
        } else {
            if(file.getName().endsWith(CLASS_SUFFIX)){
                outputList.add(file.getPath());
            }
        }
        return outputList;
    }
    
    private String rewritePackageName(String packageName){
        if(packageName == null){
            return null;
        }
        return packageName.replace(DOT, SLASH);
    }
}
