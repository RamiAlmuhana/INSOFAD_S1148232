import { Product } from "./product.model";

export class Order {
  public id: number;
  public name: string;
  public infix: string;
  public last_name: string; // Zorg ervoor dat dit type correct is (was eerder number, dat lijkt een fout te zijn)
  public zipcode: string;
  public houseNumber: number;   // Veranderd van house_number naar houseNumber
  public notes: string;
  public orderDate: string;
  public products: Product[];

}
