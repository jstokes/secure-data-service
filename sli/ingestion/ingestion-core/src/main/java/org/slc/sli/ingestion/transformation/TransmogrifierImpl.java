/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.slc.sli.ingestion.transformation;

import java.util.Collections;
import java.util.List;

import org.slc.sli.ingestion.Job;
import org.slc.sli.ingestion.WorkNote;

/**
 * basic implementation of Transmogrifier
 * 
 * @author dduran
 * 
 */
public final class TransmogrifierImpl implements Transmogrifier {
    
    private final Job job;
    private final List<TransformationStrategy> transformationStrategies;
    private WorkNote workNote;
    
    // private constructor. use static factory method to create instances.
    private TransmogrifierImpl(Job job, List<TransformationStrategy> transformationStrategies) {
        
        this.job = job;
        
        // never have null collection
        if (transformationStrategies == null) {
            transformationStrategies = Collections.emptyList();
        }
        
        this.transformationStrategies = transformationStrategies;
    }
    
    /**
     * Constructor for transmogrifier that incorporates work notes.
     * 
     * @param job current job to be processed.
     * @param transformationStrategies set of transformation strategies to be executed.
     * @param workNote collection and range transformation strategy should act upon.
     */
    private TransmogrifierImpl(Job job, List<TransformationStrategy> transformationStrategies, WorkNote workNote) {
        
        this.job = job;
        
        if (transformationStrategies == null) {
            transformationStrategies = Collections.emptyList();
        }
        
        this.transformationStrategies = transformationStrategies;
        this.workNote = workNote;
    }
    
    /**
     * Static factory method for creating a basic Transmogrifier
     * 
     * @param batchJobId
     * @param transformationStrategies
     * @return
     */
    public static Transmogrifier createInstance(Job job, List<TransformationStrategy> transformationStrategies) {
        return new TransmogrifierImpl(job, transformationStrategies);
    }
    
    public static Transmogrifier createInstance(Job job, List<TransformationStrategy> transformationStrategies, WorkNote workNote) {
        return new TransmogrifierImpl(job, transformationStrategies, workNote);
    }
    
    @Override
    public void executeTransformations() {
        for (TransformationStrategy transformationStrategy : transformationStrategies) {
            if (workNote == null) {
                transformationStrategy.perform(job);
            } else {
                transformationStrategy.perform(job, workNote);
            }            
        }
    }
    
}
