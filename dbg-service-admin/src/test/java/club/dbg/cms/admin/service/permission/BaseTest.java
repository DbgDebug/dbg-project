package club.dbg.cms.admin.service.permission;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {

    @Test
    public void arrayTest() {
        String[] strings = new String[5];
        System.out.println(strings.length);
    }

    @Test
    public void listTest() {
        Solution solution = new Solution();
        MountainArray mountainArray = new MountainArray();
        int index = solution.findInMountainArray(3, mountainArray);
        System.out.println("topIndex: " + solution.topIndex);
        System.out.println("topValue: " + solution.topValue);
        System.out.println("index: " + index);
        if (index >= 0) {
            System.out.println("value: " + mountainArray.get(index));
        }
    }
}

class Solution {
    Integer topIndex;
    Integer topValue = null;

    // 先用二分查找找出 i, 再查找 target 的最小下标
    public int findInMountainArray(int target, MountainArray mountainArr) {
        int l = 0;
        topIndex = searchIndex(l, mountainArr.length() - 1, mountainArr);
        if (topValue != null && target == topValue) {
            return topIndex;
        }

        int startValue = mountainArr.get(0);
        if(target == startValue){
            return 0;
        }

        int index = searchTargetAsc(target, 0, topIndex + 1, mountainArr);
        if(index != -1){
            return index;
        }
        index = searchTargetDesc(target, topIndex, mountainArr.length(), mountainArr);
        return index;
    }

    int searchIndex(int l, int h, MountainArray mountainArr) {
        int index = (l + h) / 2;
        if (index < 1 || index >= mountainArr.length() - 1) {
            return -1;
        }
        int topValue = mountainArr.get(index);
        int lowValue = mountainArr.get(index - 1);
        int heightValue = mountainArr.get(index + 1);
        if (topValue > lowValue && topValue > heightValue) {
            this.topValue = topValue;
            return index;
        }
        if (topValue > lowValue && topValue < heightValue) {
            return searchIndex(index, h, mountainArr);
        }
        if (topValue < lowValue && topValue > heightValue) {
            return searchIndex(l, index, mountainArr);
        }
        return -1;
    }

    int searchTargetAsc(int target, int l, int h, MountainArray mountainArr) {
        int index = (l + h) / 2;
        int value = mountainArr.get(index);
        if (target == value) {
            return index;
        }
        if (index <= 0 || index >= topIndex) {
            return -1;
        }
        if (target > value) {
            return searchTargetAsc(target, index, h, mountainArr);
        } else {
            return searchTargetAsc(target, l, index, mountainArr);
        }
    }

    int searchTargetDesc(int target, int l, int h, MountainArray mountainArr) {
        int index = (l + h) / 2;
        int value = mountainArr.get(index);
        if (target == value) {
            return index;
        }
        if (index <= topIndex || index >= mountainArr.length() - 1) {
            return -1;
        }

        if (target < value) {
            return searchTargetDesc(target, index, h, mountainArr);
        } else {
            return searchTargetDesc(target, l, index, mountainArr);
        }
    }
}

class MountainArray {
    private final int[] arr = {0,1,0};

    public MountainArray() {
    }

    public int get(int index) {
        return arr[index];
    }

    public int length() {
        return arr.length;
    }
}
