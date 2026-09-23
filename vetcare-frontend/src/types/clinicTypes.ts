export interface ClinicServiceCategoryResponse {
  id: string; // UUID
  name: string;
  description?: string;
  slug: string;
  createdAt: string;
  updatedAt: string;
  displayOrder?: number;
  createdBy?: string;
  active: boolean;
}

export interface ClinicServiceResponse {
  id: string; // UUID
  name: string;
  description?: string;
  image?: string;
  slug: string;
  createdAt: string;
  updatedAt: string;
  displayOrder?: number;
  createdBy?: string;
  active: boolean;
  categoryId: string; // UUID
}