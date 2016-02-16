package ravensproject;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayDeque;

public class VisualFigure {
	
	private int[][] numrep;
	private int height;
	private int width;
	public VisualFigure(BufferedImage img) {
		height = img.getHeight();
		width = img.getWidth();
		numrep = new int[height][width];
		byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		
		final int pixelLength = 4;
		for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
			numrep[row][col] = (((int) pixels[pixel + 1] & 0xff) < 255 ? 1 : 0 );
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}
	}
	
	public VisualFigure(int[][] arr, int height, int width) {
		this.height = height;
		this.width = width;
		numrep = arr;
	}
	public VisualFigure add(VisualFigure B) {
		//int[][] Barr = B.getNumrep();
		int[][] toCopy = B.getNumrep();
		int[][] Barr = new int[height][];
		for(int i = 0; i < height; i++)
		    Barr[i] = toCopy[i].clone();
		if((height != B.getHeight()) || (width != B.getWidth())) {
			return null;
		}
		
		for (int pixel = 0, row = 0, col = 0; pixel < width*height; pixel += 1) {
			if (Barr[row][col] + numrep[row][col] == 2)
				Barr[row][col] = 1;
			else
				Barr[row][col] = Barr[row][col] + numrep[row][col];
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}
		
		return new VisualFigure(Barr, height, width);
	}
	
	public VisualFigure subtract(VisualFigure B) {
		int[][] toCopy = B.getNumrep();
		int[][] Barr = new int[height][];
		for(int i = 0; i < height; i++)
		    Barr[i] = toCopy[i].clone();
		if((height != B.getHeight()) || (width != B.getWidth())) {
			return null;
		}
		
		for (int pixel = 0, row = 0, col = 0; pixel < width*height; pixel += 1) {
			Barr[row][col] = Barr[row][col] - numrep[row][col];
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}
		
		return new VisualFigure(Barr, height, width);
	}
	
	public VisualFigure xor(VisualFigure B) {
		int[][] toCopy = B.getNumrep();
		int[][] Barr = new int[height][];
		for(int i = 0; i < height; i++)
		    Barr[i] = toCopy[i].clone();
		if((height != B.getHeight()) || (width != B.getWidth())) {
			return null;
		}
		
		for (int pixel = 0, row = 0, col = 0; pixel < width*height; pixel += 1) {
			Barr[row][col] = ((Barr[row][col] + numrep[row][col]) == 1) ? 1 : 0;
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}
		
		return new VisualFigure(Barr, height, width);
	}
	
	public VisualFigure and(VisualFigure B) {
		int[][] toCopy = B.getNumrep();
		int[][] Barr = new int[height][];
		for(int i = 0; i < height; i++)
		    Barr[i] = toCopy[i].clone();
		if((height != B.getHeight()) || (width != B.getWidth())) {
			return null;
		}
		
		for (int pixel = 0, row = 0, col = 0; pixel < width*height; pixel += 1) {
			Barr[row][col] = ((Barr[row][col] + numrep[row][col]) == 2) ? 1 : 0;
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}
		
		return new VisualFigure(Barr, height, width);
	}
	
	public int compare(VisualFigure B) {
		int[][] Barr = B.getNumrep();
		if((height != B.getHeight()) || (width != B.getWidth())) {
			return -1;
		}
		int result = 0;
		
		for (int pixel = 0, row = 0, col = 0; pixel < width*height; pixel += 1) {
			result += (Barr[row][col] == numrep[row][col]) ? 0 : 1;
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}
		return result;
	}
	
	public int[][] getNumrep() {
		return numrep;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public String print() {
		StringBuilder sb = new StringBuilder();
		for (int pixel = 0, row = 0, col = 0; pixel < width*height; pixel += 1) {
			sb.append(numrep[row][col]);
			col++;
			if (col == width) {
				col = 0;
				row++;
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	public VisualFigure getCenterObj() {
		int[][] result = new int[height][width];
		int[][] visited = new int[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				result[y][x] = 0;
				visited[y][x] = 0;
			}
		}
		
		getctrobjhelper(result, visited, width/2, height/2);
		
		return new VisualFigure(result, height, width);
	}
	
	private void getctrobjhelper(int[][] result, int[][] visited, int x, int y) {
		ArrayDeque<int[]> q = new ArrayDeque<int[]>();
		q.add(new int[]{x,y});
		visited[x][y] = 1;
		while (!q.isEmpty()) {
			int[] point = q.remove();
			x = point[0];
			y = point[1];
			result[x][y] = numrep[x][y];
			if(visited[x+1][y] == 0 && numrep[x+1][y] == 1) {
				q.add(new int[]{x+1, y});
				visited[x+1][y] = 1;
			}
			if(visited[x][y+1] == 0 && numrep[x][y+1] == 1) {
				q.add(new int[]{x, y+1});
				visited[x][y+1] = 1;
			}
			if(visited[x+1][y+1] == 0 && numrep[x+1][y+1] == 1) {
				q.add(new int[]{x+1, y+1});
				visited[x+1][y+1] = 1;
			}
			if(visited[x-1][y] == 0 && numrep[x-1][y] == 1) {
				q.add(new int[]{x-1, y});
				visited[x-1][y] = 1;
			}
			if(visited[x-1][y+1] == 0 && numrep[x-1][y+1] == 1) {
				q.add(new int[]{x-1, y+1});
				visited[x-1][y+1] = 1;
			}
			if(visited[x][y-1] == 0 && numrep[x][y-1] == 1) {
				q.add(new int[]{x, y-1});
				visited[x][y-1] = 1;
			}
			if(visited[x+1][y-1] == 0 && numrep[x+1][y-1] == 1) {
				q.add(new int[]{x+1, y-1});
				visited[x+1][y-1] = 1;
			}
			if(visited[x-1][y-1] == 0 && numrep[x-1][y-1] == 1) {
				q.add(new int[]{x-1, y-1});
				visited[x-1][y-1] = 1;
			}
			/*
			if (x == 0 && y == 0) {
				if(visited[x+1][y] == 0 && numrep[x+1][y] == 1) {
					hasNext = true;
					x += 1;
					continue;
				}
				if(visited[x][y+1] == 0 && numrep[x][y+1] == 1){
					hasNext = true;
					y += 1;
					continue;
				}
				if(visited[x+1][y+1] == 0 && numrep[x+1][y+1] == 1) {
					hasNext = true;
					x += 1;
					y += 1;
					continue;
				}
			}
			else if (x == 0) {
				if(visited[x+1][y] == 0 && numrep[x+1][y] == 1) {
					hasNext = true;
					x += 1;
					continue;
				}
				if(visited[x][y+1] == 0 && numrep[x][y+1] == 1){
					hasNext = true;
					y += 1;
					continue;
				}
				if(visited[x+1][y+1] == 0 && numrep[x+1][y+1] == 1) {
					hasNext = true;
					x += 1;
					y += 1;
					continue;
				}
				if(visited[x][y-1] == 0 && numrep[x][y-1] == 1) {
					hasNext = true;
					y -= 1;
					continue;
				}
				if(visited[x+1][y-1] == 0 && numrep[x+1][y-1] == 1) {
					hasNext = true;
					x += 1;
					y -= 1;
					continue;
				}
			}
			else if (y == 0) {
				if(visited[x+1][y] == 0 && numrep[x+1][y] == 1) {
					hasNext = true;
					x += 1;
					continue;
				}
				if(visited[x][y+1] == 0 && numrep[x][y+1] == 1){
					hasNext = true;
					y += 1;
					continue;
				}
				if(visited[x+1][y+1] == 0 && numrep[x+1][y+1] == 1) {
					hasNext = true;
					x += 1;
					y += 1;
					continue;
				}
				if(visited[x-1][y] == 0 && numrep[x-1][y] == 1) {
					hasNext = true;
					x -= 1;
					continue;
				}
				if(visited[x-1][y+1] == 0 && numrep[x-1][y+1] == 1) {
					hasNext = true;
					x -= 1;
					y += 1;
					continue;
				}
			}
			
			else if (x == (height - 1) && y == (width -1)) {
				if(visited[x-1][y] == 0 && numrep[x-1][y] == 1) {
					hasNext = true;
					x -= 1;
					continue;
				}
				if(visited[x][y-1] == 0 && numrep[x][y-1] == 1) {
					hasNext = true;
					y -= 1;
					continue;
				}
				if(visited[x-1][y-1] == 0 && numrep[x-1][y-1] == 1) {
					hasNext = true;
					x -= 1;
					y -= 1;
					continue;
				}
			}
			
			else if (x == height - 1) {
				if(visited[x-1][y] == 0 && numrep[x-1][y] == 1) {
					hasNext = true;
					x -= 1;
					continue;
				}
				if(visited[x][y+1] == 0 && numrep[x][y+1] == 1){
					hasNext = true;
					y += 1;
					continue;
				}
				if(visited[x-1][y+1] == 0 && numrep[x-1][y+1] == 1) {
					hasNext = true;
					x -= 1;
					y += 1;
					continue;
				}
				if(visited[x][y-1] == 0 && numrep[x][y-1] == 1) {
					hasNext = true;
					y -= 1;
					continue;
				}
				if(visited[x-1][y-1] == 0 && numrep[x-1][y-1] == 1) {
					hasNext = true;
					x -= 1;
					y -= 1;
					continue;
				}
			}
			else if (y == width - 1) {
				if(visited[x+1][y] == 0 && numrep[x+1][y] == 1) {
					hasNext = true;
					x += 1;
					continue;
				}
				if(visited[x][y-1] == 0 && numrep[x][y-1] == 1) {
					hasNext = true;
					y -= 1;
					continue;
				}
				if(visited[x+1][y-1] == 0 && numrep[x+1][y-1] == 1) {
					hasNext = true;
					x += 1;
					y -= 1;
					continue;
				}
				if(visited[x-1][y] == 0 && numrep[x-1][y] == 1) {
					hasNext = true;
					x -= 1;
					continue;
				}
				if(visited[x-1][y-1] == 0 && numrep[x-1][y-1] == 1) {
					hasNext = true;
					x -= 1;
					y -= 1;
					continue;
				}
			}
			else {
				if(visited[x+1][y] == 0 && numrep[x+1][y] == 1) {
					hasNext = true;
					x += 1;
					continue;
				}
				if(visited[x][y+1] == 0 && numrep[x][y+1] == 1){
					hasNext = true;
					y += 1;
					continue;
				}
				if(visited[x+1][y+1] == 0 && numrep[x+1][y+1] == 1) {
					hasNext = true;
					x += 1;
					y += 1;
					continue;
				}
				if(visited[x-1][y] == 0 && numrep[x-1][y] == 1) {
					hasNext = true;
					x -= 1;
					continue;
				}
				if(visited[x-1][y+1] == 0 && numrep[x-1][y+1] == 1) {
					hasNext = true;
					x -= 1;
					y += 1;
					continue;
				}
				if(visited[x][y-1] == 0 && numrep[x][y-1] == 1) {
					hasNext = true;
					y -= 1;
					continue;
				}
				if(visited[x+1][y-1] == 0 && numrep[x+1][y-1] == 1) {
					hasNext = true;
					x += 1;
					y -= 1;
					continue;
				}
				if(visited[x-1][y-1] == 0 && numrep[x-1][y-1] == 1) {
					hasNext = true;
					x -= 1;
					y -= 1;
					continue;
				}
			}*/
		}
	}
}
