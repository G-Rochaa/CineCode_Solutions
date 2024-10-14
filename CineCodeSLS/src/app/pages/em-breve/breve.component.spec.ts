/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { BreveComponent } from './breve.component';

describe('BreveComponent', () => {
  let component: BreveComponent;
  let fixture: ComponentFixture<BreveComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BreveComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BreveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
