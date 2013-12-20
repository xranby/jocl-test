/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package javaapplicationocltest2;

import com.jogamp.opencl.CLBuffer;
import com.jogamp.opencl.CLCommandQueue;
import com.jogamp.opencl.CLContext;
import com.jogamp.opencl.CLKernel;
import com.jogamp.opencl.CLProgram;

import java.nio.FloatBuffer;

/**
 *
 * @author mfur
 */
public class OCLTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CLContext context = CLContext.create();
        try {
            System.out.println(context);

            try {
                CLProgram program = context.createProgram(OCLTest.class.getResourceAsStream("KernelTemplate.cl"));

                program.build();
               
                System.out.println(program.getBuildLog());

                CLCommandQueue queue = context.getMaxFlopsDevice().createCommandQueue();

                int size = 16;
                CLBuffer<FloatBuffer> buffer = context.createFloatBuffer(size);

                CLKernel initPopulation = program.createCLKernel("fill");
                initPopulation.setArgs(buffer, size, 42);

                //queue.putWork(work);
                queue.put1DRangeKernel(initPopulation, 0, 16, 16);
                queue.putReadBuffer(buffer, true);

                FloatBuffer ib = buffer.getBuffer();
                while (ib.hasRemaining()) {
                    float value = ib.get();

                    System.out.println(value + "\t");
                }
            } catch (Exception e) {
            }

        } finally {
            context.release();
        }

    }
} 
