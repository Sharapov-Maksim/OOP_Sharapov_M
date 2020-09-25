public class Main {
    public static void main (String args[]) {
        int arr[] = {15, 4, -1, 3, 100, 100500, -123, 5, 5, 5, 18, -404, 505, 112};
        sort(arr);
        for (int i = 0; i < arr.length; i++){
            System.out.println(arr[i]);
        }
    }
    public static int[] sort (int []arr){
        Heap.heapsort(arr);
        return arr;
    }
}


class Heap {
    int heap[];
    int lstid = 0;

    private Heap(int size){
        heap = new int[size];
    }
    private void swap (int id1, int id2){
        int tmp = heap[id1];
        heap[id1] = heap[id2];
        heap[id2] = tmp;
    }

    private void siftUp(int v){
        if (v != 0){
            int f = (v - 1) / 2; //parent element
            if (heap[v]<heap[f]) {
                swap(v, f);
                siftUp(f);
            }
        }
    }

    private void add (int X){
        heap[lstid++] = X;
        siftUp(lstid-1);
    }

    private int minID(int a, int b, int ida, int idb) {
        return (a < b ? ida : idb);
    }

    private void siftDown(int v){
        int ls = v * 2 + 1; //left son
        int rs = v * 2 + 2; //right son
        if (ls >= lstid) {
            return;
        }
        if ((rs == lstid) && (heap[ls]>=heap[v])) {
            return;
        }
        if ((rs == lstid) && (heap[ls]<heap[v])) {
            swap(v, ls);
            return;
        }
        if ((rs < lstid) && (heap[ls] >= heap[v]) && (heap[rs] >= heap[v])) {
            return;
        }
        int minimal = minID(heap[ls], heap[rs], ls, rs);
        swap(v, minimal);
        siftDown(minimal);
    }

    private int extractmin(){
        int res = heap[0];
        swap(0, --lstid);
        if (lstid!=0) {
            siftDown(0);
        }
        return res;

    }

    public static void heapsort(int arr[]){
        Heap myHeap = new Heap(arr.length);

        for (int i = 0; i < arr.length; i++){
            myHeap.add(arr[i]);
        }
        for (int i = 0; i < arr.length; i++){
            arr[i] = myHeap.extractmin();
        }
    }

}


