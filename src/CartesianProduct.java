
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
 
public class CartesianProduct {
	public List<?> product(List<List<?>> a) {
        if (a.size() >= 2) {
            List<?> product = a.get(0);
            for (int i = 1; i < a.size(); i++) {
                product = product(product, a.get(i));
            }
            return product;
        }
      System.err.println("empty list");
        return emptyList();
    }

	
	  private <A, B> List<?> product(List<A> a, List<B> b) {
        return of(a.stream()
                .map(e1 -> of(b.stream().map(e2 -> asList(e1, e2)).collect(toList())).orElse(emptyList()))
                .flatMap(List::stream)
                .collect(toList())).orElse(emptyList());
    }
	  public  <T> List<List<T>> cartesianProduct2 (List<List<T>> lists) {

		    List<List<T>> product = new ArrayList<List<T>>();

		    for (List<T> list : lists) {

		        List<List<T>> newProduct = new ArrayList<List<T>>();

		        for (T listElement : list) {

		            if (product.isEmpty()) {

		                List<T> newProductList = new ArrayList<T>();
		                newProductList.add(listElement);
		                newProduct.add(newProductList);
		            } else {

		                for (List<T> productList : product) {

		                    List<T> newProductList = new ArrayList<T>(productList);
		                    newProductList.add(listElement);
		                    newProduct.add(newProductList);
		                }
		            }
		        }

		        product = newProduct;
		    }

		    return product;
		}
	  
}
 