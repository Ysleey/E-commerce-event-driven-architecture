export type UserRole = 'ADMIN' | 'SALES' | 'LOGISTICS' | 'CUSTOMER';

export interface UserProfile {
  username: string;
  roles: UserRole[];
}
