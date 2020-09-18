public class Main {
    public static void main (String args[]) {
        int arr[] = {15, 4, -1, 3, 100, 10500, -123, 5, 5, 5, 18, -404, 505, 112};
        Heap myHeap = new Heap(arr.length);
        myHeap.heapsort(arr);
        for (int i = 0; i < arr.length; i++){
            System.out.println(arr[i]);
        }
    }
}


class Heap {
    int heap[];
    int lstid = 0;

    public Heap(int size){
        heap = new int[size];
    }
    public void swap (int id1, int id2){
        int tmp = heap[id1];
        heap[id1] = heap[id2];
        heap[id2] = tmp;
    }

    public void siftUp(int v){
        if (v != 0){
            int f = (v - 1) / 2; //parent element
            if (heap[v]<heap[f]) {
                swap(v, f);
                siftUp(f);
            }
        }
    }

    public void add (int X){
        heap[lstid++] = X;
        siftUp(lstid-1);
    }

    public int minID(int a, int b, int ida, int idb) {
        return (a < b ? ida : idb);
    }

    public void siftDown(int v){
        int ls = v * 2 + 1; //left son
        int rs = v * 2 + 2; //right son
        if (ls >= lstid) return;
        if ((rs == lstid) && (heap[ls]>=heap[v])) return;
        if ((rs == lstid) && (heap[ls]<heap[v])) {
            swap(v, ls);
            return;
        }
        if ((rs < lstid) && (heap[ls] >= heap[v]) && (heap[rs] >= heap[v])) return;
        int minimal = minID(heap[ls], heap[rs], ls, rs);
        swap(v, minimal);
        siftDown(minimal);
    }

    public int extractmin(){
        int res = heap[0];
        swap(0, --lstid);
        if (lstid!=0) siftDown(0);
        return res;

    }

    public void heapsort(int arr[]){
        for (int i = 0; i < arr.length; i++){
            add(arr[i]);
        }
        for (int i = 0; i < arr.length; i++){
            arr[i] = extractmin();
        }
    }

}


/*
tests
{5, 4, 3, 2, 1}
{1, 15, 4, 2, 3, -1, 0, -99, 100, 4, 5, 6, -8}
{15, 4, -1, 3, 100, 10500, -123, 5, 5, 5, 18, -404, 505, 112}
{1,1,1,1,1,1,1}
 */