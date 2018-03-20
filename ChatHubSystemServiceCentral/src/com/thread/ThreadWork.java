/**
 * Manipulation of Thread Business in each WorkThread
 */
package com.thread;

import java.io.IOException;

/**
 * Manipulation of Thread Business in each WorkThread
 * 
 * @author ejoverk
 *
 */
public class ThreadWork implements Runnable {

	private int serialId;

	public ThreadWork(int serialId) {
		this.serialId = serialId;
	}

	@Override
	public void run() {
		int i = 0;

		while (i < 3) {
			System.out.println("Thread 02: " + serialId);
			System.out.println(Thread.currentThread().getName());
			System.out.println("Index of Thread Inside: " + i);
			i++;
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int counter = 0;

		while (counter < 10) {
			System.out.println("------------------------------------------------");
			System.out.println("THREAD MAIN 01: " + counter);
			Thread thread = new Thread(new ThreadWork(counter));
			thread.start();

			try {
				thread.join();
			} catch (InterruptedException ex) {
				System.out.println("Cannot wait on Subclass SubThread on ending Job");
				ex.printStackTrace();
			}

			System.out.println(Thread.currentThread().getName());
			System.out.println("------------------------------------------------");
			counter++;
		}

	}

}
