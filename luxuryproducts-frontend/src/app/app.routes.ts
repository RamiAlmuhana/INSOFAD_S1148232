import {Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {authGuard} from './auth/auth.guard';
import {LoginComponent} from './auth/login/login.component';
import {RegisterComponent} from './auth/register/register.component';
import {ProductsComponent} from "./products/products.component";
import {CartComponent} from "./cart/cart.component";
import {ProductDetailComponent} from './products/product-detail/product-detail.component';
import {ProfileComponent} from "./profile/profile.component";
import {ProfileUpdateComponent} from "./profile/profile-update/profile-update.component";
import {OrderComponent} from "./order/order.component";
import {PaymentSuccessfulComponent} from "./order/payment-successful/payment-successful.component";
import { OrderHistoryComponent } from './profile/order-history/order-history.component';
import {AdminDashboardComponent} from "./admin-dashboard/admin-dashboard.component";
import {PromoCodeAddComponent} from "./admin-dashboard/admin-promocode/promocode-add/promocode-add.component";
import {PromocodeUpdateComponent} from "./admin-dashboard/admin-promocode/promocode-update/promocode-update.component";
import {AdminPromocodeComponent} from "./admin-dashboard/admin-promocode/admin-promocode.component";
import {PromoCodeStatsComponent} from "./admin-dashboard/promocode-stats/promocode-stats.component";


export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'auth/login', component: LoginComponent},
  {path: 'auth/register', component: RegisterComponent},
  {path: 'products', component: ProductsComponent},
  {path: 'cart', component: CartComponent},
  {path: 'products/:id', component: ProductDetailComponent},
  {path: 'profile', component: ProfileComponent, canActivate: [authGuard]},
  {path: 'profile-update', component: ProfileUpdateComponent, canActivate: [authGuard]},
  {path: 'products/:id', component: ProductDetailComponent},
  {path: 'order', component: OrderComponent, canActivate: [authGuard]},
  {path: 'order-history', component: OrderHistoryComponent, canActivate: [authGuard] },
  {path: 'paymentsuccessful', component: PaymentSuccessfulComponent, canActivate: [authGuard]},
  {path: 'orders', component: OrderComponent, canActivate: [authGuard]},
  { path: 'admin', component: AdminDashboardComponent, canActivate: [authGuard], data: { roles: ['admin'] } },
  {path: 'admin/promocode-list/promocode-add', component: PromoCodeAddComponent, canActivate: [authGuard]},
  {path: 'admin/promocode-stats', component: PromoCodeStatsComponent, canActivate: [authGuard]},
  {path: 'admin/promocode-list/promocode-update/:id', component: PromocodeUpdateComponent, canActivate: [authGuard]},
  {path: 'admin/promocode-list', component: AdminPromocodeComponent, canActivate: [authGuard]},
];






