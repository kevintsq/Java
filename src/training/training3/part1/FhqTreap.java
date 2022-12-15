package training.training3.part1;

public class FhqTreap {
    //@ public model non_null int val;
    //@ public model non_null int weight;
    //@ public model nullable FhqTreap leftTreap;
    //@ public model nullable FhqTreap rightTreap;
    private final int val;
    private final int weight;
    private final FhqTreap leftTreap;
    private final FhqTreap rightTreap;

    public FhqTreap(int val, int weight, FhqTreap leftTreap, FhqTreap rightTreap) {
        this.val = val;
        this.weight = weight;
        this.leftTreap = leftTreap;
        this.rightTreap = rightTreap;
    }

    /*@ public normal_behavior
      @ assignable \nothing;
      @ ensures ((val < key && rightTreap != null) ==> \result == new FhqTreap(val, weight, leftTreap, rightTreap.split_l(key)));
      @ ensures ((val < key && rightTreap == null) ==> \result == new FhqTreap(val, weight, leftTreap, null));
      @ ensures ((val >= key && leftTreap != null) ==> \result == leftTreap.split_l(key));
      @ ensures ((val >= key && leftTreap == null) ==> \result == null);
      @*/
    public FhqTreap split_l(int key) {
        if (val < key) {
            if (rightTreap == null) {
                return new FhqTreap(val, weight, leftTreap, null);
            } else {
                return new FhqTreap(val, weight, leftTreap, rightTreap.split_l(key));
            }
        } else {
            if (leftTreap == null) {
                return null;
            } else {
                return leftTreap.split_l(key);
            }
        }
    }

    /*@ public normal_behavior
      @ assignable \nothing;
      @ ensures ((val > key && leftTreap != null) ==> \result == new FhqTreap(val, weight, leftTreap.split_r(key)));
      @ ensures ((val > key && leftTreap == null) ==> \result == new FhqTreap(val, weight, leftTreap, null));
      @ ensures ((val <= key && rightTreap != null) ==> \result == rightTreap.split_r(key));
      @ ensures ((val <= key && rightTreap == null) ==> \result == null);
      @*/
    public FhqTreap split_r(int key) {
        if (val > key) {
            if (leftTreap == null) {
                return new FhqTreap(val, weight, null, rightTreap);
            } else {
                return new FhqTreap(val, weight, leftTreap.split_r(key), rightTreap);
            }
        } else {
            if (rightTreap == null) {
                return null;
            }
            return rightTreap.split_r(key);
        }
    }

    /*@ public normal_behavior
      @ requires nodeA != null && nodeB != null;
      @ ensures ((nodeA.weight < nodeB.weight) ==> \result == new FhqTreap(nodeA.val, nodeA.weight, nodeA.leftTreap, merge(nodeA.rightTreap, nodeB)));
      @ ensures ((nodeA.weight >= nodeB.weight) ==> \result == new FhqTreap(nodeB.val, nodeB.weight, merge(nodeA, nodeB.leftTreap), nodeB.rightTreap));
      @ also
      @ public normal_behavior
      @ requires !(nodeA == null && nodeB == null) && !(nodeA != null && nodeB != null);
      @ assignable \nothing;
      @ ensures ((nodeA == null && nodeB != null) ==> \result == nodeB);
      @ ensures ((nodeA != null && nodeB == null) ==> \result == nodeA);
      @ also
      @ public exceptional_behavior
      @ requires nodeA == null && nodeB == null;
      @ signals_only MergeNodeNullException;
      @*/
    public static FhqTreap merge(FhqTreap nodeA, FhqTreap nodeB) throws MergeNodeNullException {
        if (nodeA != null && nodeB != null) {
            if (nodeA.weight < nodeB.weight) {
                return new FhqTreap(nodeA.val, nodeA.weight, nodeA.leftTreap, merge(nodeA.rightTreap, nodeB));
            } else {
                return new FhqTreap(nodeB.val, nodeB.weight, merge(nodeA, nodeB.leftTreap), nodeB.rightTreap);
            }
        } else {
            if (nodeA != null) {
                return nodeA;
            } else if (nodeB != null) {
                return nodeB;
            } else {
                throw new MergeNodeNullException();
            }
        }
    }

    public FhqTreap insert(FhqTreap node) throws MergeNodeNullException {
        return merge(this.split_l(node.getVal()), merge(node, this.split_r(node.getVal())));
    }

    public void print() {
        int leftV = 0;
        int rightV = 0;
        if (leftTreap != null) {
            leftV = leftTreap.getVal();
        }
        if (rightTreap != null) {
            rightV = rightTreap.getVal();
        }
        System.out.println(val + ".." + weight + ".." + leftV + ".." + rightV);
        if (leftTreap != null) {
            leftTreap.print();
        }
        if (rightTreap != null) {
            rightTreap.print();
        }
    }

    public int getVal() {
        return val;
    }

}