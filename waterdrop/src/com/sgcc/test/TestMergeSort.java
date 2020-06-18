package com.sgcc.test;

import java.util.Arrays;

public class TestMergeSort {
	public static void main(String ... args){
		int[] arr = {8,5,1,7,9,6,3};
		System.out.println(Arrays.toString(arr));
		TestMergeSort.merge_sort(arr);
		System.out.println(Arrays.toString(arr));
		
		/**
		 * java如何判断一个数据是否为基础类型
		 */
		for(int i = 0  ; i < arr.length ; i++){
//			System.out.println(arr[i] instanceof int);
		}
	}
	
	// 归并排序（Java-迭代版）
	public static void merge_sort(int[] arr) {
	    int len = arr.length;
	    int[] result = new int[len];
	    int block, start;

	    // 原版代码的迭代次数少了一次，没有考虑到奇数列数组的情况
	    for(block = 1; block < len*2; block *= 2) {
	        for(start = 0; start <len; start += 2 * block) {
	            int low = start;
	            int mid = (start + block) < len ? (start + block) : len;
	            int high = (start + 2 * block) < len ? (start + 2 * block) : len;
	            //两个块的起始下标及结束下标
	            int start1 = low, end1 = mid;
	            int start2 = mid, end2 = high;
	            //开始对两个block进行归并排序
	            while (start1 < end1 && start2 < end2) {
	            result[low++] = arr[start1] < arr[start2] ? arr[start1++] : arr[start2++];
	            }
	            while(start1 < end1) {
	            result[low++] = arr[start1++];
	            }
	            while(start2 < end2) {
	            result[low++] = arr[start2++];
	            }
	        }
	    int[] temp = arr;
	    arr = result;
	    result = temp;
	    }
	    result = arr;       
	}

}
