kernel void fill(global float* a, const int size, const int value) {
    int index = get_global_id(0);
    if (index >= size) {
        return;
    }
   
    a[index] = 0.5f;
} 
